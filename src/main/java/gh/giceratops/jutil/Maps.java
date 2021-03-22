package gh.giceratops.jutil;

import java.util.Map;

public class Maps {

    protected Maps() {
    }

    public static boolean isEmpty(final Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
}
