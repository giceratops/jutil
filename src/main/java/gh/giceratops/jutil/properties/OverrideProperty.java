package gh.giceratops.jutil.properties;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class OverrideProperty<T> {

    private final String key;
    private final Supplier<T> def;
    private final Function<String, T> decoder;
    private final Function<T, String> encoder;
    private T value;

    public OverrideProperty(final String key, final Function<String, T> decoder) {
        this(key, (T) null, decoder);
    }

    public OverrideProperty(final String key, final T def, final Function<String, T> decoder) {
        this(key, def, decoder, String::valueOf);
    }

    public OverrideProperty(final String key, final Supplier<T> def, final Function<String, T> decoder) {
        this(key, def, decoder, String::valueOf);
    }

    public OverrideProperty(final String key, final T def, final Function<String, T> decoder, final Function<T, String> encoder) {
        this(key, () -> def, decoder, encoder);
    }

    public OverrideProperty(final String key, final Supplier<T> def, final Function<String, T> decoder, final Function<T, String> encoder) {
        this.key = Objects.requireNonNull(key, "key");
        this.def = def;
        this.decoder = Objects.requireNonNull(decoder, "decoder");
        this.encoder = Objects.requireNonNull(encoder, "encoder");
    }

    String key() {
        return this.key;
    }

    T def() {
        return this.def.get();
    }

    T fromString(final String value) {
        if (this.value == null) {
            this.value = this.decoder.apply(value);
        }

        return this.value;
    }

    boolean isPresent() {
        return this.value != null;
    }

    T get() {
        return this.value;
    }

    public void clear() {
        this.value = null;
    }

    public String toString(final T t) {
        return this.encoder.apply(t);
    }
}
