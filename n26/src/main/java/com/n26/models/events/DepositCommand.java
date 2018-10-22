package com.n26.models.events;

import com.n26.models.transactions.CashTransaction;
import com.n26.models.transactions.Transaction;
import com.n26.utils.TimeUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

class DepositCommand implements TransactionCommand {

    private final BigDecimal amount;
    private final LocalDateTime expiry;
    private TimeUtils timeUtils;

    public DepositCommand(BigDecimal amount, LocalDateTime expiry, TimeUtils timeUtils) {
        this.amount = amount;
        this.expiry = expiry;
        this.timeUtils = timeUtils;
    }

    @Override
    public Transaction toTransaction() {
        return new CashTransaction(amount);
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(timeUtils.durationFromNow(expiry), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        long diff = timeUtils.durationFromNow(expiry) - timeUtils.durationFromNow(((DepositCommand) o).expiry);
        return diff < 0 ? -1 : (diff ==0 ? 0: 1);
    }
}
