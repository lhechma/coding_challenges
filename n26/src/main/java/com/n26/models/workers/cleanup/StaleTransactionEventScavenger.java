package com.n26.models.workers.cleanup;

import com.n26.models.StatisticsCalculator;
import com.n26.models.events.TransactionCommand;
import com.n26.models.queues.MessageQueue;
import com.n26.models.transactions.Transaction;
import org.springframework.scheduling.annotation.Async;

public class StaleTransactionEventScavenger {

    private final MessageQueue<TransactionCommand> transactionEventHeap;
    private StatisticsCalculator statisticsCalculator;

    public StaleTransactionEventScavenger(MessageQueue<TransactionCommand> delayQueue, StatisticsCalculator statisticsCalculator) {
        this.transactionEventHeap = delayQueue;
        this.statisticsCalculator = statisticsCalculator;
    }

    @Async
    public void scavenge() {
        try {
            TransactionCommand expired = null;
            while ((expired = transactionEventHeap.pull()) != null) {
                Transaction transaction = expired.toTransaction();
                statisticsCalculator.update(transaction.negate());
            }
        } catch (Exception e) {
            //Send it to stderr for the time being
            System.err.println(e);
        }
    }
}
