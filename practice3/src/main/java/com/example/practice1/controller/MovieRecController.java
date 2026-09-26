package com.example.practice1.controller;

import com.example.practice1.record.Movie;
import com.example.practice1.service.MovieService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movie")
public class MovieRecController {

    private final MovieService movieService;

    public MovieRecController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public List<Movie> getRec() {
        return movieService.getMovies();
    }

    @PostMapping
    public Movie createMovie(@RequestBody Movie movie) {
        return movieService.createMovie(movie);
    }

    @PatchMapping
    public String updateMovie(
            @RequestParam String title,
            @RequestParam String description
    ) {
        return movieService.updateMovie(title, description);
    }

    @DeleteMapping
    public String deleteMovie(@RequestParam int year) {
        return movieService.deleteMovie(year);
    }
}
