package gh.giceratops.jutil;

import java.util.Collection;

public class Collections {

    protected Collections() {
    }

    public static boolean isEmpty(final Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
}
