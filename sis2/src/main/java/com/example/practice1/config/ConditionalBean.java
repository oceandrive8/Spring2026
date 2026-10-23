package com.example.practice1.config;

import com.example.practice1.movie.app.MovieService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;

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
                        "Number of movies: " + movieService.list(Pageable.ofSize(1)).getTotalElements()
        );
    }
}