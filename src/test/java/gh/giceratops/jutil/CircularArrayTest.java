package gh.giceratops.jutil;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CircularArrayTest {

    private final CircularArray<Integer> array;

    public CircularArrayTest() {
        this.array = new CircularArray<>(5);

        this.array.push(1);
        this.array.push(2);
        this.array.push(3);
        this.array.push(4);
        this.array.push(5);
        this.array.push(6);
        this.array.push(7);
    }

    @Test
    public void getArrayTest() {
        final Integer[] expected = new Integer[] { 3, 4, 5, 6, 7 };

        assertArrayEquals(expected, this.array.toArray());
    }

    @Test
    public void getTest() {
        assertEquals(7, (int) this.array.get(0));
        assertEquals(6, (int) this.array.get(1));
        assertEquals(4, (int) this.array.get(3));
        assertEquals(5, (int) this.array.get(2));
        assertEquals(3, (int) this.array.get(4));

        assertEquals(7, (int) this.array.get(5));
        assertEquals(6, (int) this.array.get(6));
        assertEquals(5, (int) this.array.get(7));
        assertEquals(4, (int) this.array.get(8));
        assertEquals(3, (int) this.array.get(9));
    }

    @Test
    public void boundsTest() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> this.array.get(-1));
    }
}
