package com.example.practice1.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "movie.app")
@Validated
public record MovieProperties(
        @NotBlank
        String name,

        @Min(1)
        int maxMovies,

        @NotBlank
        String welcomeMessage
) {
}
