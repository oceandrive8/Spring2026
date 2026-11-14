package com.example.practice1.movie.web;

public record MovieResponse(
        Long id,
        String title,
        String genre,
        Integer year,
        String description
) {
}