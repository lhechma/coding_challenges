package com.n26.models.transactions;

import com.n26.models.Statistics;

public class PurgeTransaction implements Transaction {

    @Override
    public Statistics combine(Statistics stats) {
        return null;
    }

    @Override
    public Transaction negate() {
        return this;
    }

    @Override
    public Statistics toStatistics() {
        return null;
    }
}
