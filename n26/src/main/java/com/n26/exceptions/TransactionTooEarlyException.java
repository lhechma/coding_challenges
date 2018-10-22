package com.n26.exceptions;

public class TransactionTooEarlyException extends RuntimeException {
    public TransactionTooEarlyException(String message) {
        super(message);
    }
}
