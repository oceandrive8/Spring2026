package com.example.practice1.movie.web;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A movie in the catalog")
public record MovieResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "The Fellowship of the Ring") String title,
        @Schema(example = "Fantasy") String genre,
        @Schema(example = "2001") Integer year,
        @Schema(example = "A hobbit sets out to destroy a ring") String description
) {
}