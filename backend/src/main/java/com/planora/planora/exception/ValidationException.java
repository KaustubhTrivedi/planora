package com.planora.planora.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;
import java.util.List;

/**
 * Exception thrown when validation fails
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends RuntimeException {
    
    private final List<String> errors;
    
    public ValidationException(String message) {
        super(message);
        this.errors = Arrays.asList(message);
    }
    
    public ValidationException(List<String> errors) {
        super(String.join(", ", errors));
        this.errors = errors;
    }
    
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        this.errors = Arrays.asList(message);
    }
    
    public List<String> getErrors() {
        return errors;
    }
}