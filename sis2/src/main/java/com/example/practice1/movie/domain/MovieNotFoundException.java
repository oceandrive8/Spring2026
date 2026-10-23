package com.example.practice1.movie.domain;

public class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException(Long id) {
        super("Movie with id " + id + " not found");
    }
}