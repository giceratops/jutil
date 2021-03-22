package gh.giceratops.jutil.function;

@FunctionalInterface
public interface FloatConsumer {

    default FloatConsumer andThen(final FloatConsumer after) {
        return value -> {
            this.accept(value);
            after.accept(value);
        };
    }

    void accept(final float value);
}
