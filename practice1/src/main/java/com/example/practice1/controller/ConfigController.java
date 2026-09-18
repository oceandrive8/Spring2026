package com.example.practice1.controller;

import com.example.practice1.config.MovieProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/config")
public class ConfigController {

    private final MovieProperties movieProperties;

    public ConfigController(MovieProperties movieProperties) {
        this.movieProperties = movieProperties;
    }

    @GetMapping
    public MovieProperties getConfig() {
        return movieProperties;
    }
}
