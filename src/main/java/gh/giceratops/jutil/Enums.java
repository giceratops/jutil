package gh.giceratops.jutil;

import gh.giceratops.jutil.function.ByteSupplier;

import java.util.Optional;
import java.util.function.IntSupplier;

public class Enums {

    protected Enums() {
    }

    public static <E extends Enum<E>> Enum<E> max(final Enum<E> e1, final Enum<E> e2) {
        if (e1 == null) {
            return e2;
        } else if (e2 == null) {
            return e1;
        } else {
            return e1.ordinal() >= e2.ordinal() ? e1 : e2;
        }
    }

    public static <E extends Enum<E>> Enum<E> min(final Enum<E> e1, final Enum<E> e2) {
        if (e1 == null) {
            return e2;
        } else if (e2 == null) {
            return e1;
        } else {
            return e1.ordinal() < e2.ordinal() ? e1 : e2;
        }
    }

    public static <E extends Enum<E>> String toString(final Enum<E> e) {
        return Strings.enumName(e);
    }

    public static <E extends Enum<E> & ByteSupplier> Optional<E> forValue(final byte b, Class<E> eClass) {
        for (var value : eClass.getEnumConstants()) {
            if (value.getAsByte() == b) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }

    public static <E extends Enum<E> & IntSupplier> Optional<E> forValue(final int i, Class<E> eClass) {
        for (var value : eClass.getEnumConstants()) {
            if (value.getAsInt() == i) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }
}
