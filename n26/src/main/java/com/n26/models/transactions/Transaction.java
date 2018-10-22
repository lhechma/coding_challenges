package com.n26.models.transactions;

import com.n26.models.Statistics;

import java.math.BigDecimal;
import java.util.Optional;

public interface Transaction {

    Statistics combine(Statistics stats);

    default Transaction negate() {
        return new NegateTransaction(this);
    }

    Statistics toStatistics();

    default Optional<BigDecimal> getAmount() {
        return Optional.empty();
    }
}
