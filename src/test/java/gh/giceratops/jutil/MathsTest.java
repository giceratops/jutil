package gh.giceratops.jutil;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MathsTest {

    @Test
    public void testCalcBatch() {
        assertEquals(5, Maths.calcBatch(3, 5));
        assertEquals(10, Maths.calcBatch(5, 5));
        assertEquals(10, Maths.calcBatch(6, 5));
        assertEquals(125, Maths.calcBatch(123, 5));
    }
}
