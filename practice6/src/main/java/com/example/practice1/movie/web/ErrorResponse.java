package com.example.practice1.movie.web;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "Unified error format")
public record ErrorResponse(
        @Schema(example = "404") int status,
        @Schema(example = "Movie 99 not found") String message,
        @Schema(description = "When the error happened", example = "2026-11-14T12:44:13") LocalDateTime timestamp,
        @Schema(description = "Validation errors by field",
                example = "{\"year\":\"must be greater than or equal to 1888\"}")
        Map<String, String> errors) { }