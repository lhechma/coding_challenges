package com.n26.models.transactions;

import com.n26.models.Statistics;

import java.math.BigDecimal;
import java.util.Optional;

public class CashTransaction implements Transaction {

    final BigDecimal amount;

    public CashTransaction(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public Statistics combine(Statistics stats) {
        BigDecimal min = stats.getMin().min(amount);
        BigDecimal max = stats.getMax().max(amount);
        return new Statistics(stats.getSum().add(amount), min, max, stats.getCount() + 1, stats.getMin(), stats.getMax());
    }

    @Override
    public Statistics toStatistics() {
        return Statistics.from(amount);
    }

    @Override
    public Optional<BigDecimal> getAmount() {
        return Optional.of(amount);
    }
}
