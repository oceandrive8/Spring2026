package com.example.practice1.rating;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "rating.client")
public record RatingClientProperties(String baseUrl, String apiKey,
                                     Duration connectTimeout, Duration readTimeout) { }
