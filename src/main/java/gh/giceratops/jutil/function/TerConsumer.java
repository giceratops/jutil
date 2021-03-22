package gh.giceratops.jutil.function;

@FunctionalInterface
public interface TerConsumer<T, U, V> {

    default TerConsumer<T, U, V> andThen(final TerConsumer<? super T, ? super U, ? super V> after) {
        return (t, u, v) -> {
            this.accept(t, u, v);
            after.accept(t, u, v);
        };
    }

    void accept(final T t, final U u, final V v);
}
