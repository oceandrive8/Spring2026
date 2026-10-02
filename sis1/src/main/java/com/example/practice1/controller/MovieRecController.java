package com.example.practice1.controller;

import com.example.practice1.dto.MovieRequest;
import com.example.practice1.dto.MovieResponse;
import com.example.practice1.dto.MovieUpdateRequest;
import com.example.practice1.record.Movie;
import com.example.practice1.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.example.practice1.exception.MovieNotFoundException;
import com.example.practice1.exception.MovieConflictException;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.net.URI;

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

    @GetMapping("/{id}")
    public MovieResponse getMovieById(@PathVariable int id) {
        Movie movie = movieService.getMovieById(id);

        if (movie == null) {
            throw new MovieNotFoundException("Movie with id " + id + " was not found");
        }

        return new MovieResponse(
                movie.id(),
                movie.title(),
                movie.genre(),
                movie.year(),
                movie.description()
        );
    }

    @PostMapping
    public ResponseEntity<MovieResponse> createMovie(
            @Valid @RequestBody MovieRequest request
    ) {
        Movie movie = new Movie(
                0,
                request.title(),
                request.genre(),
                request.year(),
                request.description()
        );

        Movie createdMovie = movieService.createMovie(movie);

        MovieResponse response = new MovieResponse(
                createdMovie.id(),
                createdMovie.title(),
                createdMovie.genre(),
                createdMovie.year(),
                createdMovie.description()
        );

        URI location = URI.create("/api/movie/" + createdMovie.id());

        return ResponseEntity
                .created(location)
                .body(response);
    }

    @PutMapping("/{id}")
    public MovieResponse updateMovie(
            @PathVariable int id,
            @Valid @RequestBody MovieUpdateRequest request
    ) {
        Movie movie = new Movie(
                id,
                request.title(),
                request.genre(),
                request.year(),
                request.description()
        );

        Movie updatedMovie = movieService.updateMovie(id, movie);

        if (updatedMovie == null) {
            throw new MovieNotFoundException(
                    "Movie with id " + id + " was not found"
            );
        }

        return new MovieResponse(
                updatedMovie.id(),
                updatedMovie.title(),
                updatedMovie.genre(),
                updatedMovie.year(),
                updatedMovie.description()
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie(@PathVariable int id) {
        boolean deleted = movieService.deleteMovie(id);

        if (!deleted) {
            throw new MovieNotFoundException(
                    "Movie with id " + id + " was not found"
            );
        }
    }
}
