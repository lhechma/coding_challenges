package com.n26.exceptions;

public class TransactionTooLateException extends RuntimeException{
    public TransactionTooLateException(String message) {
        super(message);
    }
}
