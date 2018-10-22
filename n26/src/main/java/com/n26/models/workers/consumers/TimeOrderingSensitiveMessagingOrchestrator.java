package com.n26.models.workers.consumers;

import com.n26.models.StatisticsCalculator;
import com.n26.models.events.TransactionCommand;
import com.n26.models.queues.MessageQueue;

import java.util.function.BiFunction;

//Handles out of order transactions as to process the oldest one, not necessarily the latest one that arrived
public class TimeOrderingSensitiveMessagingOrchestrator extends AbstractMessagingOrchestratorAdapter {


    private final BiFunction<MessageQueue<TransactionCommand>, TransactionCommand, TransactionCommand> withTimeOrder;
    //O(n) = n log n
    private MessageQueue<TransactionCommand> heap;


    public TimeOrderingSensitiveMessagingOrchestrator(MessageQueue<TransactionCommand> fifo, final MessageQueue<TransactionCommand> heap, MessageQueue<TransactionCommand> delay, final StatisticsCalculator calculator) {
        super(fifo, delay, calculator);
        this.heap = heap;
        withTimeOrder = appendToQueue.andThen(readFromQueue);
    }

    @Override
    protected void onTransaction(TransactionCommand transaction) {
        if(transaction.isPurge()){
            heap.clear();
        }
        TransactionCommand oldestTransaction = withTimeOrder.apply(heap, transaction);
        dispatchToCalculator.apply(oldestTransaction);
    }

}
