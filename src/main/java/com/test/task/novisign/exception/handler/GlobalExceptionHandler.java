package com.test.task.novisign.exception.handler;

import com.test.task.novisign.exception.NotFoundException;
import com.test.task.novisign.model.response.ExceptionResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.time.DateTimeException;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String NOT_FOUND_REASON = "Entity is not found";
    private static final String PARSING_FAILURE_REASON = "Parsing failure";
    private static final String DATA_INTEGRITY_VIOLATION_REASON = "Data integrity violation";

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

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<ExceptionResponse> handleDateTimeParseException(DateTimeParseException exception) {
        return ResponseEntity.badRequest()
                .body(ExceptionResponse.builder()
                        .reason(PARSING_FAILURE_REASON)
                        .messages(List.of(exception.getMessage() + ": " + exception.getParsedString()))
                        .build());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> handleDataIntegrityException(DataIntegrityViolationException exception) {
        return ResponseEntity.badRequest()
                .body(ExceptionResponse.builder()
                        .reason(DATA_INTEGRITY_VIOLATION_REASON)
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
