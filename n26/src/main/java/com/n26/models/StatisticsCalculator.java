package com.n26.models;

import com.n26.models.transactions.Transaction;

import java.util.Optional;

import static java.util.Optional.empty;

public class StatisticsCalculator {

    private volatile Optional<Statistics> statistics = empty();

    public Statistics getStatistics() {
        return statistics.orElse(Statistics.NO_STATS);
    }

    public void update(Transaction transaction) {
        Statistics newStatistic = this.statistics.map(x -> x.absorb(transaction)).orElse(transaction.toStatistics());
        this.statistics = Optional.ofNullable(newStatistic);
    }

    public void reset() {
        this.statistics = empty();
    }
}
