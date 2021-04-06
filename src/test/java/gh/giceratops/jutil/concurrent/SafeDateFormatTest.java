package gh.giceratops.jutil.concurrent;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class SafeDateFormatTest {

    @Test
    void testConstructorThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            @SuppressWarnings("unused")
            final var sdf = new SafeDateFormat();
        });
    }

    @Test
    void testFormat() {
        final var sdf = new SafeDateFormat("yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd", "HH:mm:ss");

        assertEquals("1970/01/01 01:00:00", sdf.format(Date.from(Instant.EPOCH)));
    }

    @Test
    void testParse() throws ParseException {
        final var sdf = new SafeDateFormat("yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd", "HH:mm:ss");

        assertEquals(Date.from(Instant.EPOCH), sdf.parse("1970/01/01 01:00:00"));
    }
}
