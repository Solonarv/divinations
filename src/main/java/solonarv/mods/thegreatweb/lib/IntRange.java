package solonarv.mods.thegreatweb.lib;

import java.util.*;

/**
 *
 */
public class IntRange extends AbstractCollection<Integer> {
    private static final String ERROR_IMMUTABLE = "IntRange is an immutable collection";
    public final int min;
    public final int max;

    public IntRange(int min, int max) {
        boolean flip = min > max;
        this.min = flip ? max : min;
        this.max = flip ? min : max;
    }

    public int size() {
        return max - min;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        if (! (o instanceof Integer))
            return false;
        int i = (Integer) o;
        return i >= min && i <= max;
    }

    public int random(Random rng) {
        return rng.nextInt(size() + 1) + min;
    }

    public IntRange withMin(int newMin) {
        return new IntRange(newMin, max);
    }

    public IntRange withMax(int newMax) {
        return new IntRange(min, newMax);
    }

    /**
     * Averages this range's minimum and maximum using the given weighting.
     * intRange.weightedAverage(0) -> intRange.min
     * intRange.weightedAverage(0.5) -> halfway point between intRange.min and intRange.max
     * intRange.weightedAverage(1) -> intRange.max
     * @param u a double value
     * @return this.max * u + this.min * (1-u)
     */
    public int weightedAverage(double u) {
        return Math.toIntExact(Math.round(u * max + (1 - u) * min));
    }

    @Override
    public String toString() {
        return String.format("[%d, %d]", min, max);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        if (! (other instanceof IntRange))
            return false;
        IntRange ir = (IntRange) other;
        return min == ir.min && max == ir.max;
    }

    @Override
    public int hashCode() {
        return min ^ max;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new IntIterator(min, max);
    }

    @Override
    public boolean add(Integer integer) {
        throw new UnsupportedOperationException(ERROR_IMMUTABLE);
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException(ERROR_IMMUTABLE);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c)
            if (!contains(o))
                return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Integer> c) {
        throw new UnsupportedOperationException(ERROR_IMMUTABLE);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException(ERROR_IMMUTABLE);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException(ERROR_IMMUTABLE);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException(ERROR_IMMUTABLE);
    }

    public static IntRange around(int centerChunkX, int radius) {
        return new IntRange(centerChunkX - radius, centerChunkX + radius);
    }

    private static class IntIterator implements Iterator<Integer> {
        private final int max;
        private int current;

        public IntIterator(int min, int max) {
            this.max = max;
            this.current = min;
        }

        @Override
        public boolean hasNext() {
            return current <= max;
        }

        @Override
        public Integer next() {
            if (!hasNext()) throw new NoSuchElementException();
            return current++;
        }
    }
}
