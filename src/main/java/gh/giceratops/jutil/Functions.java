package gh.giceratops.jutil;

import java.util.function.Function;

public class Functions {

    protected Functions() {
    }

    public static <T, R> Function<T, R> unchecked(final CheckedFunction<T, R> func) {
        return (T t) -> {
            try {
                return func.apply(t);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    public interface CheckedFunction<T, R> {

        R apply(final T t) throws Exception;
    }
}
