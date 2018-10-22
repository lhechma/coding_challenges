package com.n26.models.events;

import com.n26.models.transactions.Transaction;

import java.util.concurrent.Delayed;

public interface TransactionCommand extends Delayed {

    Transaction toTransaction();

    //A little cheating :-)
    default boolean isPurge(){
        return false;
    }

}
