package gh.giceratops.jutil;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SecureRandomTest {

    private static final int LOWER = 5;
    private static final int UPPER = 10;

    private final boolean debug;

    public SecureRandomTest() {
        this.debug = false;
    }

    private Set<Integer> rangeTest(final int lower, final int upper) {
        final TreeSet<Integer> results = new TreeSet<>(Comparator.naturalOrder());
        final int loops = (Math.abs(upper - lower) + 1) * 100;

        for (int i = 0; i < loops; i++) {
            results.add(Randomizer.nextInt(lower, upper));
        }

        final int min = results.first();
        final int max = results.last();
        final int count = results.size();

        assertEquals(Math.min(lower, upper), min, "min");
        assertEquals(Math.max(lower, upper), max, "max");
        assertEquals(max - min + 1, count, "count [" + min + ", " + max + "] >> " + results);
        return results;
    }

    public Map<Integer, Integer> spreadTest(final int lower, final int upper) {
        final int expected = 100000;

        final Map<Integer, Integer> results = new HashMap<>();
        final int loops = (Math.abs(upper - lower) + 1) * expected;

        for (int i = 0; i < loops; i++) {
            final int result = Randomizer.nextInt(lower, upper);
            final int count = results.getOrDefault(result, 0) + 1;
            results.put(result, count);
        }

        results.forEach((key, value) -> {
            final double p = Math.abs(expected - value) * 100d / expected;

            assertTrue(p <= 3.0, "More than 3% (" + p + "%) spread on " + key);
        });
        return results;
    }

    private void debug(final int lower, final int upper, final Object... msg) {
        if (this.debug) {
            System.out.printf("[%d, %d]%n", lower, upper);
            for (Object o : msg) {
                System.out.printf("%s%n", o);
            }
        }
    }

    private void test(final int lower, final int upper) {
        this.debug(
                lower, upper,
                "> Range:  " + this.rangeTest(lower, upper),
                "> Spread: " + this.spreadTest(lower, upper)
        );
    }

    @Test
    public void rangeTest() {
        this.test(LOWER, UPPER);
    }

    @Test
    public void reverseRange() {
        this.test(UPPER, LOWER);
    }

    @Test
    public void negativeRange() {
        this.test(-UPPER, -LOWER);
    }

    @Test
    public void reverseNegativeRange() {
        this.test(-LOWER, -UPPER);
    }

    @Test
    public void combinedRange() {
        this.test(-LOWER, LOWER);
        this.test(LOWER, -LOWER);
        this.test(-UPPER, UPPER);
        this.test(UPPER, -UPPER);
    }

    @Test
    public void singleton() {
        this.test(LOWER, LOWER);
        this.test(UPPER, UPPER);
    }
}
