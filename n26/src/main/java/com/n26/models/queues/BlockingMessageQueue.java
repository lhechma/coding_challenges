package com.n26.models.queues;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Delayed;

public class BlockingMessageQueue<T extends Delayed> implements MessageQueue<T> {

    private final BlockingQueue<T> queue;

    public BlockingMessageQueue(BlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void push(T t) {
        try {
            queue.put(t);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public T pull() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public T peek() {
        return queue.poll();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public void clear() {
        queue.clear();
    }
}
