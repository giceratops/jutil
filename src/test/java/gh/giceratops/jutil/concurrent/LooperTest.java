package gh.giceratops.jutil.concurrent;

import gh.giceratops.jutil.function.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class LooperTest {

    @Test
    public void CheckException() {
        final var exception = new Exception("CheckException");
        final Task task = () -> {
            throw exception;
        };

        assertTimeoutPreemptively(Duration.ofMillis(100), () -> Looper.builder("CheckException")
                .once()
                .addLogger((log) -> {
                    assertEquals(log.run(), 1, "run");
                    assertEquals(log.errors(), 1, "errors");
                    assertFalse(log.isSuccess(), "isSuccess");
                    assertEquals(log.cause(), exception, "CheckException");
                })
                .run(task)
        );
    }
}
