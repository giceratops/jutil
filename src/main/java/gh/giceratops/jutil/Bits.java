package gh.giceratops.jutil;

public class Bits {

    protected Bits() {
    }

    public static byte shiftLeft(final byte bits, int shift) {
        shift %= 8;
        return (byte) (((bits & 0xFF) << shift) | ((bits & 0xFF) >>> (8 - shift)));
    }

    public static byte shiftRight(final byte bits, int shift) {
        shift %= 8;
        return (byte) (((bits & 0xFF) >>> shift) | ((bits & 0xFF) << (8 - shift)));
    }
}
