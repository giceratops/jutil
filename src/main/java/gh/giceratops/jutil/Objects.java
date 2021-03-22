package gh.giceratops.jutil;

public class Objects {

    protected Objects() {
    }

    public static <E> E orDefault(final E e, final E def) {
        if (e == null) {
            return def;
        }
        return e;
    }
}
