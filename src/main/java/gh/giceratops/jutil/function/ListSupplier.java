package gh.giceratops.jutil.function;

import java.util.List;
import java.util.function.Supplier;

@FunctionalInterface
public interface ListSupplier<T> extends Supplier<List<T>> {

    @Override
    List<T> get();
}
