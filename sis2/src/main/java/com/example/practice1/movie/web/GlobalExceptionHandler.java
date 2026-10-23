package com.example.practice1.movie.web;

import com.example.practice1.movie.domain.MovieConflictException;
import com.example.practice1.movie.domain.MovieNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(MovieNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler(MovieConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(MovieConflictException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    // Domain rule violations, e.g. blank title or invalid release year
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleDomainRule(IllegalArgumentException ex) {
        return build(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), null);
    }

    // Bean Validation errors from @Valid request bodies
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> errors.putIfAbsent(e.getField(), e.getDefaultMessage()));
        return build(HttpStatus.BAD_REQUEST, "Validation failed", errors);
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message, Map<String, String> errors) {
        return ResponseEntity.status(status)
                .body(new ErrorResponse(status.value(), message, LocalDateTime.now(), errors));
    }
}
