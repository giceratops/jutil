package gh.giceratops.jutil;

import java.lang.reflect.Field;

public class Reflect {

    protected Reflect() {
    }

    private static Field field(final Object obj, final String name) throws NoSuchFieldException {
        final var field = obj.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    public static Object getField(final Object obj, final String field) {
        try {
            return field(obj, field).get(obj);
        } catch (final Exception e) {
            throw new RuntimeException("Unable to access field [" + obj.getClass().getSimpleName() + "." + field + "]",
                    e);
        }
    }

    public static void setField(final Object obj, final String field, final Object value) {
        try {
            field(obj, field).set(obj, value);
        } catch (final Exception e) {
            throw new RuntimeException("Unable to access field [" + obj.getClass().getSimpleName() + "." + field + "]",
                    e);
        }
    }
}
