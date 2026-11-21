package com.example.practice1.movie.web;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Payload for updating a movie (the current director is kept)")
public record MovieUpdateRequest(
        @Schema(example = "The Fellowship of the Ring")
        @NotBlank(message = "Title is required")
        String title,

        @Schema(example = "Fantasy")
        @NotBlank(message = "Genre is required")
        String genre,

        @Schema(example = "2001", minimum = "1888")
        @Min(value = 1888, message = "Year must be 1888 or later")
        int year,

        @Schema(example = "A hobbit sets out to destroy a ring")
        @NotBlank(message = "Description is required")
        String description
) {
}