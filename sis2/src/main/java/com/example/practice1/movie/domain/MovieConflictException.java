package com.example.practice1.movie.domain;

public class MovieConflictException extends RuntimeException {

    public MovieConflictException(String message) {
        super(message);
    }
}