package gh.giceratops.jutil.concurrent;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class DaemonThreadFactory implements ThreadFactory {

    private final ThreadFactory defaultFactory = Executors.defaultThreadFactory();

    @Override
    public Thread newThread(final @NotNull Runnable runnable) {
        final var thread = this.defaultFactory.newThread(runnable);
        thread.setDaemon(true);
        return thread;
    }
}
