package gh.giceratops.jutil;

import java.util.function.BiFunction;

public class BiFunctions {

    public static  <U, T, R> BiFunction<U, T, R> unchecked(final CheckedBiFunction<U, T, R> func) {
        return (final U u, final T t) -> {
            try {
                return func.apply(u, t);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static interface CheckedBiFunction<U, T, R> {

        R apply(final U u, final T t) throws Exception;
    }
}
