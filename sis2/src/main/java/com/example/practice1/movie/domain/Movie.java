package com.example.practice1.movie.domain;

public record Movie(
        int id,
        String title,
        String genre,
        int year,
        String description
) {
}
