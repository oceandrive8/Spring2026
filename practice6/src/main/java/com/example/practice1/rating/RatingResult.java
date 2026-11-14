package com.example.practice1.rating;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Movie rating from the external service")
public record RatingResult(
        String title,
        @Schema(description = "Null when the fallback is used", example = "7.6") Double rating,
        @Schema(description = "REMOTE or FALLBACK", example = "REMOTE") String source) { }
