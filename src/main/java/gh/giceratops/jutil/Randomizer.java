package gh.giceratops.jutil;

import java.util.Base64;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class Randomizer {

    private static Supplier<Random> random;

    protected Randomizer() {
    }

    public static void random(final Random rand) {
        random(() -> rand);
    }

    public static void random(final Supplier<Random> rand) {
        Objects.requireNonNull(rand.get(), "random.get()");
        random = rand;
    }

    public static Random random() {
        if (random == null) {
            random = ThreadLocalRandom::current;
        }

        return random.get();
    }

    public static byte nextByte() {
        return (byte) random().nextInt(255);
    }

    public static byte[] nextBytes(final int length) {
        final byte[] bytes = new byte[length];
        random().nextBytes(bytes);
        return bytes;
    }

    public static void nextBytes(final byte[] buffer) {
        random().nextBytes(buffer);
    }

    public static int nextInt() {
        return random().nextInt();
    }

    public static int nextInt(final int bound) {
        return random().nextInt(bound);
    }

    public static int nextInt(final int lower, final int upper) {
        return lower < upper
                ? random().nextInt(upper - lower + 1) + lower
                : random().nextInt(lower - upper + 1) + upper;
    }

    public static long nextLong() {
        return random().nextLong();
    }

    public static String UUID() {
        return UUID(50);
    }

    public static String UUID(final int length) {
        return Base64.getEncoder().encodeToString(nextBytes(length));
    }
}
