package com.kuehne_nagel.model;

import java.util.Arrays;

public class NonNegativePerformanceMetricConstraint {

    public boolean allNoneNegative(double... performances) {
        double[] perfs = performances;
        return Arrays.stream(perfs).noneMatch(num -> num < 0);
    }
}
