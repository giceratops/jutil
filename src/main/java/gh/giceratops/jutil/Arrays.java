package gh.giceratops.jutil;

public class Arrays {

    protected Arrays() {
    }

    public static boolean isEmpty(final byte[] arr) {
        return arr == null || arr.length == 0;
    }

    public static <E> boolean isEmpty(final E[] arr) {
        return arr == null || arr.length == 0;
    }

    public static <E> boolean contains(final E[] arr, final E element) {
        if (arr == null) {
            return false;
        } else if (element == null) {
            for (final E e : arr) {
                if (e == null) {
                    return true;
                }
            }
        } else {
            for (final E e : arr) {
                if (element == e || element.equals(e)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean contains(final long[] arr, final long element) {
        if (arr == null || arr.length == 0) {
            return false;
        }

        for (final long e : arr) {
            if (element == e) {
                return true;
            }
        }
        return false;
    }

    public static byte[] multiply(final byte[] arr, final int times) {
        if (arr == null || arr.length == 0) {
            return new byte[0];
        }

        final byte[] mul = new byte[arr.length * times];
        for (int i = 0; i < times; i++) {
            System.arraycopy(arr, 0, mul, i * times, arr.length);
        }
        return mul;
    }

    public static <E> E getInRange(final E[] arr, final int index) {
        if (index <= 0) {
            return arr[0];
        } else if (index >= arr.length) {
            return arr[arr.length - 1];
        } else {
            return arr[index];
        }
    }

    public static int getInRange(final int[] arr, final int index) {
        if (index <= 0) {
            return arr[0];
        } else if (index >= arr.length) {
            return arr[arr.length - 1];
        } else {
            return arr[index];
        }
    }

    public static long getInRange(final long[] arr, final int index) {
        if (index <= 0) {
            return arr[0];
        } else if (index >= arr.length) {
            return arr[arr.length - 1];
        } else {
            return arr[index];
        }
    }

    public static int length(final byte[] arr) {
        if (arr == null) {
            return 0;
        }
        return arr.length;
    }

    public static byte[] growTo(final byte[] src, final int size) {
        if (src == null) {
            return new byte[size];
        }

        if (size <= src.length) {
            return src;
        }

        return java.util.Arrays.copyOf(src, size);
    }

    public static byte[] prepBatch(final byte[] src, int size, final int batchSize) {
        size = Maths.calcBatch(size, batchSize);
        return growTo(src, size);
    }
}
