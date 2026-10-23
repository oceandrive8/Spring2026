package com.example.practice1.exception;

public class MovieConflictException extends RuntimeException {

    public MovieConflictException(String message) {
        super(message);
    }
}