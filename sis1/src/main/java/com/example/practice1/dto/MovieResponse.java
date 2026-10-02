package com.example.practice1.dto;

public record MovieResponse(
        int id,
        String title,
        String genre,
        int year,
        String description
) {
}