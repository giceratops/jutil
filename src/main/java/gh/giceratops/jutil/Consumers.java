package gh.giceratops.jutil;

import java.util.function.Consumer;

public class Consumers {

    protected Consumers() {
    }

    public static <T> Consumer<T> unchecked(final CheckedConsumer<T> consumer) {

        return (t) -> {
            try {
                consumer.accept(t);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    public interface CheckedConsumer<T> {

        void accept(final T t) throws Exception;
    }

}
