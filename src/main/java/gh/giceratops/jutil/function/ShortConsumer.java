package gh.giceratops.jutil.function;

@FunctionalInterface
public interface ShortConsumer {

    default ShortConsumer andThen(final ShortConsumer after) {
        return value -> {
            this.accept(value);
            after.accept(value);
        };
    }

    void accept(final short value);
}
