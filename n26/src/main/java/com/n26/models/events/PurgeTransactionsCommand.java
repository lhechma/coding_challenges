package com.n26.models.events;

import com.n26.models.transactions.PurgeTransaction;
import com.n26.models.transactions.Transaction;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

class PurgeTransactionsCommand implements TransactionCommand {

    @Override
    public Transaction toTransaction() {
        return new PurgeTransaction();
    }

    @Override
    public boolean isPurge() {
        return true;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return 0;
    }

    @Override
    public int compareTo(Delayed o) {
        return -1;
    }
}
