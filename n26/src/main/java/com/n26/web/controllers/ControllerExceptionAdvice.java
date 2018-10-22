package com.n26.web.controllers;

import com.n26.web.exception.ExceptionResolver;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ValidationException;
import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class ControllerExceptionAdvice extends ResponseEntityExceptionHandler {


    @Autowired
    //Employ the Open Closed principle here
    private ExceptionResolver exceptionResolver;

    public ControllerExceptionAdvice(ExceptionResolver exceptionResolver) {
        this.exceptionResolver = exceptionResolver;
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        HttpStatus httpStatus = ExceptionUtils.getThrowableList(ex).stream().map(exceptionResolver::from).filter(Objects::nonNull).findFirst().orElse(BAD_REQUEST);
        return ResponseEntity.status(httpStatus).build();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, null, headers, status, request);
    }

    @ExceptionHandler({ValidationException.class})
    protected ResponseEntity<Object> handleValidationException(
            ValidationException ex) {
        HttpStatus httpStatus = ExceptionUtils.getThrowableList(ex).stream().map(exceptionResolver::from).filter(Objects::nonNull).findFirst().orElse(BAD_REQUEST);
        return ResponseEntity.status(httpStatus).build();
    }


}
