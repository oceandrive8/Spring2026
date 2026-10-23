package com.example.practice1.movie.domain;

import java.time.Year;

public record Movie(
        Long id,
        String title,
        String genre,
        Integer releaseYear,
        String description,
        Director director
) {
    public Movie {
        if (title == null || title.isBlank())
            throw new IllegalArgumentException("Title must not be blank");
        if (releaseYear != null && (releaseYear < 1888 || releaseYear > Year.now().getValue()))
            throw new IllegalArgumentException("Release year is not valid: " + releaseYear);
    }

    public Movie withDescription(String newDescription) {
        return new Movie(id, title, genre, releaseYear, newDescription, director);
    }
}
