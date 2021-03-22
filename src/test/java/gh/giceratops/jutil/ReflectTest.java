package gh.giceratops.jutil;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ReflectTest {

    private static class T {
       private String field;
    }

    @Test
    public void readTest() {
        final var t = new T();
        t.field = "value";

        assertEquals("value", Reflect.getField(t, "field"));
    }

    @Test
    public void writeTest() {
        final var t = new T();
        Reflect.setField(t, "field", "value");
        assertEquals("value", t.field);
    }

    @Test
    public void getThrowsTest() {
        final var t = new T();
        assertThrows(RuntimeException.class, () -> {
            Reflect.getField(t, "nonExistingField");
        });
    }

    @Test
    public void setThrowsTest() {
        final var t = new T();
        assertThrows(RuntimeException.class, () -> {
            Reflect.setField(t, "nonExistingField", "value");
        });
    }

    @Test
    public void typeTest() {
        final var t = new T();
        assertThrows(RuntimeException.class, () -> {
            Reflect.setField(t, "field", 1234);
        });
    }
}
