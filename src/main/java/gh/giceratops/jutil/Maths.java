package gh.giceratops.jutil;

public class Maths {

    protected Maths() {
    }

    public static int calcBatch(final int pos, final int batchSize) {
        return ((pos / batchSize) + 1) * batchSize;
    }
}
