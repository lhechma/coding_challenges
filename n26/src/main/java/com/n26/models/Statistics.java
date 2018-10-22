package com.n26.models;

import com.n26.models.transactions.Transaction;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static java.math.BigDecimal.ZERO;

public class Statistics {
    public static final Statistics NO_STATS = new Statistics(ZERO, ZERO, ZERO, 0, ZERO, ZERO);
    @Getter
    private final BigDecimal sum;
    @Getter
    private final BigDecimal max;
    @Getter
    private final BigDecimal min;
    @Getter
    private final int count;
    //Need a cache to revert to the previous Max and min in case of an expired transaction
    @Getter
    private final BigDecimal previousMax;
    @Getter
    private final BigDecimal previousMin;

    public Statistics(BigDecimal sum, BigDecimal min, BigDecimal max, int count, BigDecimal previousMin, BigDecimal previousMax) {
        this.sum = sum;
        this.max = max;
        this.min = min;
        this.count = count;
        this.previousMin = previousMin;
        this.previousMax = previousMax;
    }

    public static Statistics from(BigDecimal amount) {
        return new Statistics(amount, amount, amount, 1, amount, amount);
    }

    //Test very small transactions 0.0001
    public Statistics absorb(Transaction transaction) {
        return transaction.combine(this);
    }

    public BigDecimal getAverage() {
        if (count != 0) {
            MathContext mc = new MathContext(5, RoundingMode.HALF_UP);
            return sum.divide(BigDecimal.valueOf(count), mc);
        } else
            return BigDecimal.ZERO;
    }

}
