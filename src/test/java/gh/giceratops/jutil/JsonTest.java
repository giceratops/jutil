package gh.giceratops.jutil;

import java.awt.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Make sure that we can parse java.awt.Point.
 */
public class JsonTest {

    private static final Point POINT_OBJ = new Point(1, 2);
    private static final String POINT_JSON = "{\"x\": 1.0, \"y\": 2.0}";

    @Test
    public void stringifyPointTest() {
        assertEquals(POINT_JSON.replaceAll("\\s", ""), Json.stringify(POINT_OBJ).replaceAll("\\s", ""));
    }

    @Test
    public void parsePointTest() {
        assertEquals(POINT_OBJ, Json.parse(POINT_JSON, Point.class));
    }
}
