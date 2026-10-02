package com.example.practice1.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record MovieRequest(
        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Genre is required")
        String genre,

        @Min(value = 1888, message = "Year must be 1888 or later")
        int year,

        @NotBlank(message = "Description is required")
        String description
) {
}