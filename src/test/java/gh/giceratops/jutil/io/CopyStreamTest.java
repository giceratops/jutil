package gh.giceratops.jutil.io;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class CopyStreamTest {

    @Test
    public void writeByte() {
        final var dst = new byte[3];
        try (final var cs = new CopyStream(dst, 0)) {
            cs.write(1);
            cs.write(2);
            cs.write(3);
        } finally {
            assertArrayEquals(new byte[] { 1, 2, 3 }, dst);
        }
    }

    @Test
    public void writeByteOffset() {
        final var dst = new byte[3];
        try (final var cs = new CopyStream(dst, 1)) {
            cs.write(1);
            cs.write(2);
        } finally {
            assertArrayEquals(new byte[] { 0, 1, 2 }, dst);
        }

    }

    // @Test
    // public void writeByteOutOfBounds() {
    // final var dst = new byte[1];
    // final var cs = new CopyStream(dst, 0);
    // cs.write(1);
    //
    // assertThrows(ArrayIndexOutOfBoundsException.class,
    // () -> cs.write(2)
    // );
    // }

    @Test
    public void writeArray() {
        final var src = new byte[] { 1, 2, 3 };
        final var dst = new byte[3];
        try (final var cs = new CopyStream(dst, 0)) {
            cs.write(src, 0, src.length);
        } finally {
            assertArrayEquals(src, dst);
        }
    }

    @Test
    public void writeArrayOffset1() {
        final var src = new byte[] { 1, 2 };
        final var dst = new byte[3];
        try(final var cs = new CopyStream(dst, 1)){
            cs.write(src, 0, src.length);
        } finally {
            assertArrayEquals(new byte[] { 0, 1, 2 }, dst);
        }
    }

    @Test
    public void writeArrayOffset2() {
        final var src = new byte[] { 1, 2, 3 };
        final var dst = new byte[3];
        try (final var cs = new CopyStream(dst, 0)) {
            cs.write(src, 1, src.length - 1);
        } finally {
            assertArrayEquals(new byte[] { 2, 3, 0 }, dst);
        }
    }

    // @Test
    // public void writeArrayOutOfBounds() {
    // final var src = new byte[]{1,2,3,4};
    // final var dst = new byte[3];
    // final var cs = new CopyStream(dst, 0);
    //
    // assertThrows(ArrayIndexOutOfBoundsException.class,
    // () -> cs.write(src, 0, src.length)
    // );
    // }

    @Test
    public void writeByteAndArray1() {
        final var dst = new byte[3];
        final var src = new byte[] { 2, 3 };
        try (final var cs = new CopyStream(dst, 0)) {
            cs.write(1);
            cs.write(src, 0, src.length);
        } finally {
            assertArrayEquals(new byte[] { 1, 2, 3 }, dst);
        }
    }

    @Test
    public void writeByteAndArray2() {
        final var dst = new byte[3];
        final var src = new byte[] { 1, 2 };
        try(final var cs = new CopyStream(dst, 0)) {
            cs.write(src, 0, src.length);
            cs.write(3);
        } finally {
            assertArrayEquals(new byte[] { 1, 2, 3 }, dst);
        }
    }

    @Test
    public void close() {
        final var dst = new byte[0];
        final var cs = new CopyStream(dst, 0);
        cs.close();
    }
}
