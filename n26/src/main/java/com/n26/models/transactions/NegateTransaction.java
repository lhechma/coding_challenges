package com.n26.models.transactions;

import com.n26.models.Statistics;

import java.math.BigDecimal;
import java.util.Optional;

public class NegateTransaction implements Transaction {

    private Transaction delegate;


    public NegateTransaction(Transaction delegate) {
        this.delegate = delegate;
    }


    @Override
    public Statistics combine(Statistics stats) {
        if (getAmount().isPresent()) {
            BigDecimal amount = this.getAmount().get();
            return new Statistics(stats.getSum().add(amount), stats.getPreviousMin(), stats.getPreviousMax(), stats.getCount() - 1, stats.getPreviousMin(), stats.getPreviousMax());
        }
        return delegate.combine(stats);
    }

    @Override
    public Statistics toStatistics() {
        return null;
    }

    @Override
    public Optional<BigDecimal> getAmount() {
        return delegate.getAmount().map(BigDecimal::negate);
    }
}
