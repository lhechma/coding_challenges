package com.n26.models.workers.consumers;

import com.n26.models.StatisticsCalculator;
import com.n26.models.events.TransactionCommand;
import com.n26.models.queues.MessageQueue;

public class FifoMessagingOrchestrator extends AbstractMessagingOrchestratorAdapter {

    public FifoMessagingOrchestrator(MessageQueue<TransactionCommand> fifo, MessageQueue<TransactionCommand> delay, final StatisticsCalculator calculator) {
        super(fifo, delay, calculator);
    }

}

