package com.n26.models.queues;

import java.util.Queue;

public class WrapperMessageQueue<T> implements MessageQueue<T> {


    private final Queue<T> queue;

    public WrapperMessageQueue(Queue<T> queue) {
        this.queue = queue;
    }

    @Override
    public void push(T o) {
        queue.add(o);
    }

    @Override
    public T pull() {
        return queue.poll();
    }

    @Override
    public T peek() {
        return queue.peek();
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
