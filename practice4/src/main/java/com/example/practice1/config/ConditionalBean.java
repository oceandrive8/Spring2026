package com.example.practice1.config;

import com.example.practice1.service.MovieService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConditionalBean {

    @Bean
    @ConditionalOnProperty(
            name = "practice3.enabled",
            havingValue = "true"
    )
    public CommandLineRunner practice3Feature(MovieService movieService) {
        return args -> System.out.println(
                "Practice 3 conditional bean is enabled. " +
                        "Number of movies: " + movieService.getMovies().size()
        );
    }
}