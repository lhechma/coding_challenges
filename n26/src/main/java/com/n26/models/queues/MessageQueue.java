package com.n26.models.queues;

public interface MessageQueue<T> {
    void push(T t);
    T pull();
    T peek();
    boolean isEmpty();
    void clear();
}
