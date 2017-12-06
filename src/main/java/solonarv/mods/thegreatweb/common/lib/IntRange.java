package solonarv.mods.thegreatweb.common.lib;

import java.util.Random;

/**
 *
 */
public class IntRange {
    public final int min, max;

    public IntRange(int min, int max) {
        boolean flip = min > max;
        this.min = flip ? max : min;
        this.max = flip ? min : max;
    }

    public int size() {
        return max - min;
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
}
