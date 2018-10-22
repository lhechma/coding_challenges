package com.n26.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.PositiveOrZero;

@Component
@ConfigurationProperties("transaction")
@Validated
public class TransactionProperties {

    @PositiveOrZero(message = "The clock drift cannot be negative")
    @Getter
    @Setter
    long tolerableClockDriftMillis;


    @PositiveOrZero(message = "The event time to live cannot be negative")
    @Getter
    @Setter
    long eventTtlMilliSeconds;
}
