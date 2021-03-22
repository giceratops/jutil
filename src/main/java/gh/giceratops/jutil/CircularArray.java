package gh.giceratops.jutil;

import java.util.Arrays;

public class CircularArray<E> {

    private final Object[] array;
    private int index;

    @SafeVarargs
    public CircularArray(final E... e) {
        this(e.length);
        this.push(e);
    }

    @SafeVarargs
    public CircularArray(final int size, final E... e) {
        this(size);
        this.push(e);
    }

    public CircularArray(final int size) {
        this.array = new Object[size];
        this.index = 0;
    }

    public E first() {
        return this.get(this.array.length - 1);
    }

    public E last() {
        return this.get(0);
    }

    public final int size() {
        return this.array.length;
    }

    @SuppressWarnings("unchecked")
    public final E push(final E e) {
        final E o = (E) this.array[this.index];
        this.array[this.index] = e;
        this.index = (this.index + 1) % this.array.length;
        return o;
    }

    @SuppressWarnings("unchecked")
    public final void push(final E... e) {
        if (e.length > this.array.length) {
            System.arraycopy(e, e.length - this.array.length, this.array, this.index, this.array.length - this.index);
            System.arraycopy(e, e.length - this.index, this.array, 0, this.index);
        } else if (e.length <= this.array.length - this.index) {
            System.arraycopy(e, 0, this.array, this.index, e.length);
            this.index = (this.index + e.length) % this.array.length;
        } else {
            System.arraycopy(e, 0, this.array, this.index, this.array.length - this.index);
            System.arraycopy(e, this.array.length - this.index, this.array, 0, e.length - this.array.length + this.index);
            this.index = (this.index + e.length) % this.array.length;
        }
    }

    @SuppressWarnings("unchecked")
    public E get(final int index) {
        if (index < 0) {
            throw new ArrayIndexOutOfBoundsException(index);
        }

        return (E) this.array[(this.array.length + this.index - (index % this.array.length) - 1) % this.array.length];
    }

    public void clear() {
        Arrays.fill(this.array, null);
        this.index = 0;
    }

    @SuppressWarnings("unchecked")
    public E[] toArray() {
        final Object[] arr = new Object[this.array.length];
        System.arraycopy(this.array, this.index, arr, 0, arr.length - this.index);
        System.arraycopy(this.array, 0, arr, arr.length - this.index, this.index);
        return (E[]) arr;
    }

    @Override
    public String toString() {
        return String.format(
                "raw%s ordered%s",
                Arrays.toString(this.array),
                Arrays.toString(this.toArray())
        );
    }
}
