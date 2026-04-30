package com.hush0k.pirateTeam.common.random;

import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class RandomService {

    private static final double DEFAULT_SHARPNESS = 1.0;

    public int rollPercent() {
        return between(1, 100);
    }

    public boolean chance(int percent) {
        return rollPercent() <= clamp(percent, 0, 100);
    }

    public int between(int min, int max) {
        validateRange(min, max);
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public int weightedAround(int min, int max, int center) {
        return weightedAround(min, max, center, DEFAULT_SHARPNESS);
    }

    public int weightedAround(int min, int max, int center, double sharpness) {
        validateRange(min, max);
        validateSharpness(sharpness);

        int clampedCenter = clamp(center, min, max);
        int size = max - min + 1;
        double[] prefixSums = new double[size];
        double total = 0;

        for (int i = 0; i < size; i++) {
            int value = min + i;
            int distance = Math.abs(value - clampedCenter);
            double weight = 1.0 / Math.pow(distance + 1, sharpness);

            total += weight;
            prefixSums[i] = total;
        }

        double roll = ThreadLocalRandom.current().nextDouble(total);

        for (int i = 0; i < prefixSums.length; i++) {
            if (roll < prefixSums[i]) {
                return min + i;
            }
        }

        return max;
    }

    public boolean weightedSuccess(int min, int max, int center, int successFrom) {
        return weightedAround(min, max, center) >= successFrom;
    }

    public boolean weightedSuccess(int min, int max, int center, int successFrom, double sharpness) {
        return weightedAround(min, max, center, sharpness) >= successFrom;
    }

    public int clamp(int value, int min, int max) {
        return Math.clamp(value, min, max);
    }

    private void validateRange(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Минимум не может быть больше максимума");
        }
    }

    private void validateSharpness(double sharpness) {
        if (sharpness <= 0) {
            throw new IllegalArgumentException("Резкость распределения должна быть больше 0");
        }
    }

    public int simpleRandom(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
