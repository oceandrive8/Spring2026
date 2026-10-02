package com.example.practice1.record;

public record Movie(
        int id,
        String title,
        String genre,
        int year,
        String description
) {
}
