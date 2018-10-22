package com.n26.models.events;

import com.n26.configuration.TransactionProperties;
import com.n26.utils.TimeUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class TransactionCommandFactory {

    private final long eventTtlMilliSeconds;
    private TimeUtils timeUtils;

    public TransactionCommandFactory(TransactionProperties properties, TimeUtils timeUtils) {
        this.eventTtlMilliSeconds = properties.getEventTtlMilliSeconds();
        this.timeUtils = timeUtils;
    }

    public PurgeTransactionsCommand createPurgeCommand() {
        return new PurgeTransactionsCommand();
    }

    public TransactionCommand createDepositCommand(BigDecimal amount, LocalDateTime timestamp) {
        return new DepositCommand(amount, timestamp.plus(eventTtlMilliSeconds, ChronoUnit.MILLIS), timeUtils);
    }

}
