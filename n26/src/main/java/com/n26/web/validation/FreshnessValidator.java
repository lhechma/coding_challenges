package com.n26.web.validation;

import com.n26.configuration.TransactionProperties;
import com.n26.exceptions.TransactionTooEarlyException;
import com.n26.exceptions.TransactionTooLateException;
import com.n26.utils.TimeUtils;
import com.n26.web.DTO.TransactionDTO;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FreshnessValidator implements ConstraintValidator<Fresh, TransactionDTO> {

    private TransactionProperties properties;
    private TimeUtils timeUtils;

    @Autowired
    protected FreshnessValidator(TransactionProperties properties,TimeUtils timeUtils) {
        this.properties = properties;
        this.timeUtils = timeUtils;
    }

    @Override
    public boolean isValid(TransactionDTO transaction, ConstraintValidatorContext constraintValidatorContext) {
        long tolerableClockDriftMillis = properties.getTolerableClockDriftMillis();
        long eventTtlMilliSeconds = properties.getEventTtlMilliSeconds();
        long difference = timeUtils.durationToNow(transaction.getTimestamp());
        if (difference > eventTtlMilliSeconds + tolerableClockDriftMillis) {
            throw new TransactionTooLateException(transaction.toString());
        } else if (difference + tolerableClockDriftMillis < 0) {
            throw new TransactionTooEarlyException(transaction.toString());
        }
        return true;
    }

}
