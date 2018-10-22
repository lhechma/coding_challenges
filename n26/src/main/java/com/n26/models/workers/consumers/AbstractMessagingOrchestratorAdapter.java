package com.n26.models.workers.consumers;

import com.n26.models.StatisticsCalculator;
import com.n26.models.events.TransactionCommand;
import com.n26.models.queues.MessageQueue;
import org.springframework.scheduling.annotation.Async;

import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class AbstractMessagingOrchestratorAdapter implements MessagingOrchestrator {

    protected final Function<TransactionCommand, Void> dispatchToCalculator;
    protected final BiFunction<TransactionCommand, MessageQueue<TransactionCommand>, TransactionCommand> dispatchToQueue;
    protected final BiFunction<MessageQueue<TransactionCommand>, TransactionCommand, MessageQueue<TransactionCommand>> appendToQueue;
    protected final Function<MessageQueue<TransactionCommand>, TransactionCommand> readFromQueue;
    private final MessageQueue<TransactionCommand> fifo;
    private final MessageQueue<TransactionCommand> delay;

    public AbstractMessagingOrchestratorAdapter(MessageQueue<TransactionCommand> fifo, MessageQueue<TransactionCommand> delay, final StatisticsCalculator calculator) {
        this.fifo = fifo;
        this.delay = delay;
        readFromQueue = queue -> queue.pull();

        dispatchToCalculator = event -> {
            calculator.update(event.toTransaction());
            return null;
        };

        appendToQueue = (queue, command) -> {
            queue.push(command);
            return queue;
        };
        dispatchToQueue = (command, transactionCommandMessageQueue) -> {
            transactionCommandMessageQueue.push(command);
            return command;
        };
    }

    @Override
    @Async
    public void start() {
        while (true) {
            TransactionCommand nextInline = null;
            if ((nextInline = readFromQueue.apply(fifo)) != null) {
                if (nextInline.isPurge()) {
                    delay.clear();
                } else {
                   appendToQueue.apply(delay, nextInline);
                }
                onTransaction(nextInline);
            }
        }
    }

    protected void onTransaction(TransactionCommand transaction) {
        dispatchToCalculator.apply(transaction);
    }

}
