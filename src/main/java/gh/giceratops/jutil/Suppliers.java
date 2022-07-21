package gh.giceratops.jutil;

import lombok.NonNull;

import java.util.function.Supplier;

public class Suppliers {

    public static <T> Supplier<T> unchecked(@NonNull final CheckedSupplier<T> supplier) {

        return () -> {
            try {
                return supplier.get();
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    public interface CheckedSupplier<T> {

        T get() throws Exception;
    }

}
