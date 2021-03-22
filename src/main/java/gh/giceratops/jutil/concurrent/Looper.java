package gh.giceratops.jutil.concurrent;

import gh.giceratops.jutil.function.Task;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Looper implements Runnable {

    private final Task task;
    private final Log log;
    private final Consumer<Log> logger;
    private final Function<Integer, Long> tickRate;
    private final boolean loop;

    private Looper(final Builder<Log> builder, final Task task) {
        this.task = Objects.requireNonNull(task, "task");
        this.log = builder.logSupplier.get();
        this.logger = Objects.requireNonNull(builder.logger, "logger");
        this.tickRate = Objects.requireNonNull(builder.tickRate, "tickRate");
        this.loop = builder.loop;
    }

    public static Builder<Log> builder(final String name) {
        return new Builder<>(name);
    }

    @Override
    public void run() {
        try {
            this.log.starting();
            this.awaitNextRun();
            this.log.waited();
            this.task.execute();
            this.log.successfully();
        } catch (final Throwable t) {
            this.log.exceptionally(t);
        } finally {
            this.log.completed();
            this.logger.accept(this.log);
        }

        if (this.loop) {
            this.run();
        }
    }

    private void awaitNextRun() {
        final var tick = this.tickRate.apply(this.log.errors);
        final var wait = tick + this.log.endMillis - System.currentTimeMillis();
        if (wait > 0) {
            try {
                Thread.sleep(wait);
            } catch (InterruptedException ignore) {
            }
        }
    }

    public static class Log {

        protected final String name;

        protected int run, errors;
        protected long startMillis, waitMillis, endMillis;
        protected Throwable cause;

        protected Log(final String name) {
            this.name = name;
        }

        protected String name() {
            return this.name;
        }

        protected void starting() {
            this.run++;
            this.startMillis = System.currentTimeMillis();
        }

        protected void waited() {
            this.waitMillis = System.currentTimeMillis() - this.startMillis;
        }

        protected void successfully() {
            this.errors = 0;
            this.cause = null;
        }

        protected void exceptionally(final Throwable t) {
            this.errors++;
            this.cause = t;
        }

        protected void completed() {
            this.endMillis = System.currentTimeMillis();
        }

        protected int run() {
            return this.run;
        }

        protected int errors() {
            return this.errors;
        }

        protected long startMillis() {
            return this.startMillis;
        }

        protected long waitMillis() {
            return this.waitMillis;
        }

        protected long endMillis() {
            return this.endMillis;
        }

        protected boolean isSuccess() {
            return this.cause == null;
        }

        protected Throwable cause() {
            return this.cause;
        }

        @Override
        public String toString() {
            return String.format("[%s]name:%s run:%d (%d), error:%s, waited:%d, start:%s > end%s",
                    this.getClass().getSimpleName(),
                    this.name,
                    this.run,
                    this.errors,
                    this.cause,
                    this.waitMillis,
                    new Date(this.startMillis),
                    new Date(this.endMillis)
            );
        }
    }

    public static class Builder<L extends Log> {

        private final String name;
        private boolean loop;
        private Consumer<Log> logger;
        private Function<Integer, Long> tickRate;
        private Supplier<L> logSupplier;

        private Builder(final String name) {
            this.name = name;
            this.loop = true;
        }

        public Builder<L> once() {
            this.loop = false;
            return this;
        }

        @SuppressWarnings("unchecked")
        public Builder<L> addLogger(final Consumer<L> logger) {
            if (this.logger == null) {
                this.logger = (Consumer<Log>) logger;
            } else {
                this.logger = this.logger.andThen((Consumer<Log>) logger);
            }
            return this;
        }

        public Builder<L> tickRate(final Function<Integer, Long> tickRate) {
            this.tickRate = tickRate;
            return this;
        }

        @SuppressWarnings("unchecked")
        public <M extends Log> Builder<M> logSupplier(final Supplier<M> supplier) {
            this.logSupplier = (Supplier<L>) supplier;
            return (Builder<M>) this;
        }

        @SuppressWarnings("unchecked")
        public Looper wrap(final Task task) {
            this.tickRate = Objects.requireNonNullElse(this.tickRate, (i) -> 0L);
            this.logSupplier = Objects.requireNonNullElse(this.logSupplier, () -> (L) new Log(this.name));
            this.logger = Objects.requireNonNullElse(this.logger, (log) -> {
            });

            return new Looper((Builder<Log>) this, task);
        }

        public void run(final Task task) {
            this.wrap(task).run();
        }

        public Builder<L> start(final Task task) {
            return this.start(task, (r) -> new Thread(r, "Thread-" + task.toString()));
        }

        public Builder<L> start(final Task task, final ThreadFactory factory) {
            factory.newThread(this.wrap(task)).start();
            return this;
        }
    }
}
