package com.example.practice1.dto;

import com.example.practice1.entity.MovieEntity;

public record MovieResponse(
        int id,
        String title,
        String genre,
        int year,
        String description
) {
    public static MovieResponse from(MovieEntity movie) {
        return new MovieResponse(
                movie.getId().intValue(),
                movie.getTitle(),
                movie.getGenre(),
                movie.getReleaseYear(),
                movie.getDescription()
        );
    }
}