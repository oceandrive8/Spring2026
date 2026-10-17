package com.example.practice1.dto;

import com.example.practice1.entity.MovieEntity;

public record MovieResponse(Long id, String title, String genre,
                            Integer releaseYear, String directorName) {

    public static MovieResponse from(MovieEntity m) {
        return new MovieResponse(
                m.getId(), m.getTitle(), m.getGenre(), m.getReleaseYear(),
                m.getDirector() != null ? m.getDirector().getName() : null);
    }
}