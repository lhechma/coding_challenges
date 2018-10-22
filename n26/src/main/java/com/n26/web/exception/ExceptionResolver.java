package com.n26.web.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.n26.exceptions.TransactionTooEarlyException;
import com.n26.exceptions.TransactionTooLateException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Component
public class ExceptionResolver {
    private final Map<Predicate<Throwable>, HttpStatus> errorMapping = new LinkedHashMap<>();

    public ExceptionResolver() {
        //Very brittle test based on text value
        Predicate<Throwable> propertyNotFoundPredicate = throwable -> MismatchedInputException.class.isAssignableFrom(throwable.getClass())&& !throwable.getStackTrace()[2].getMethodName().equals("handleMissingInstantiator");
        Predicate<Throwable> propertyInvalidFormatPredicate = throwable -> InvalidFormatException.class.isAssignableFrom(throwable.getClass());
        Predicate<Throwable> jsonParseErrorPredicate = throwable -> JsonParseException.class.isAssignableFrom(throwable.getClass());
        Predicate<Throwable> futureTimeStampPredicate = throwable -> TransactionTooEarlyException.class.isAssignableFrom(throwable.getClass());
        Predicate<Throwable> staleTimeStampPredicate = throwable -> TransactionTooLateException.class.isAssignableFrom(throwable.getClass());
        errorMapping.put(propertyNotFoundPredicate.or(propertyInvalidFormatPredicate), UNPROCESSABLE_ENTITY);
        errorMapping.put(jsonParseErrorPredicate, BAD_REQUEST);
        errorMapping.put(futureTimeStampPredicate, UNPROCESSABLE_ENTITY);
        errorMapping.put(staleTimeStampPredicate, NO_CONTENT);
    }


    public HttpStatus from(Throwable throwable) {
        return errorMapping.entrySet().stream().filter(entry -> entry.getKey().test(throwable)).findFirst().map(Map.Entry::getValue).orElse(null);
    }
}
