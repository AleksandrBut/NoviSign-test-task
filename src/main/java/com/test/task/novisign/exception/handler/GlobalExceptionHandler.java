package com.test.task.novisign.exception.handler;

import com.test.task.novisign.exception.NotFoundException;
import com.test.task.novisign.model.response.ExceptionResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String NOT_FOUND_REASON = "Entity is not found";

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(WebExchangeBindException exception) {
        return ResponseEntity.badRequest()
                .body(buildValidationExceptionResponse(exception));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.builder()
                        .reason(NOT_FOUND_REASON)
                        .messages(List.of(exception.getMessage()))
                        .build());
    }

    private ExceptionResponse buildValidationExceptionResponse(WebExchangeBindException exception) {
        return ExceptionResponse.builder()
                .reason(exception.getReason())
                .messages(exception.getAllErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList())
                .build();
    }
}
