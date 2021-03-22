package gh.giceratops.jutil.function;

@FunctionalInterface
public interface ByteConsumer {

    default ByteConsumer andThen(final ByteConsumer after) {
        return value -> {
            this.accept(value);
            after.accept(value);
        };
    }

    void accept(final byte value);
}
