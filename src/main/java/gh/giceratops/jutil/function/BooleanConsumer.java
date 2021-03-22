package gh.giceratops.jutil.function;

@FunctionalInterface
public interface BooleanConsumer {

    default BooleanConsumer andThen(final BooleanConsumer after) {
        return value -> {
            this.accept(value);
            after.accept(value);
        };
    }

    void accept(final boolean value);
}
