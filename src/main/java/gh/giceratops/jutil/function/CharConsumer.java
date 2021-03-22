package gh.giceratops.jutil.function;

@FunctionalInterface
public interface CharConsumer {

    default CharConsumer andThen(final CharConsumer after) {
        return value -> {
            this.accept(value);
            after.accept(value);
        };
    }

    void accept(final char value);
}
