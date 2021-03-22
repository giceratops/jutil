package gh.giceratops.jutil;

public class Exceptions {

    protected Exceptions() {
    }

    public static Exception format(final String format, final Object... args) {
        return format(null, format, args);
    }

    public static Exception format(final Throwable reason, final String format, final Object... args) {
        return new Exception(String.format(format, args), reason);
    }

    public static RuntimeException runtime(final String format, final Object... args) {
        return runtime(null, format, args);
    }

    public static RuntimeException runtime(final Throwable reason, final String format, final Object... args) {
        return new RuntimeException(String.format(format, args), reason);
    }
}
