package com.example.practice1.service;

import com.example.practice1.config.MovieProperties;
import com.example.practice1.record.Movie;
import org.springframework.stereotype.Service;
import com.example.practice1.exception.MovieConflictException;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {

    private final MovieProperties movieProperties;

    private final List<Movie> movies = new ArrayList<>(
            List.of(
                    new Movie(
                            1,
                            "Howl's Moving Castle",
                            "Fantasy",
                            2004,
                            "A beautiful movie about love, identity, war, friendship and finding your place in the world. And that if it was meant to happen it would happen anyways."
                    )
            )
    );

    public MovieService(MovieProperties movieProperties) {
        this.movieProperties = movieProperties;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public Movie getMovieById(int id) {
        return movies.stream()
                .filter(movie -> movie.id() == id)
                .findFirst()
                .orElse(null);
    }

    public Movie createMovie(Movie movie) {
        boolean titleExists = movies.stream()
                .anyMatch(existingMovie ->
                        existingMovie.title().equalsIgnoreCase(movie.title())
                );

        if (titleExists) {
            throw new MovieConflictException(
                    "A movie with title '" + movie.title() + "' already exists"
            );
        }

        int nextId = movies.stream()
                .mapToInt(Movie::id)
                .max()
                .orElse(0) + 1;

        Movie createdMovie = new Movie(
                nextId,
                movie.title(),
                movie.genre(),
                movie.year(),
                movie.description()
        );

        movies.add(createdMovie);
        return createdMovie;
    }

    public String updateMovie(String title, String description) {
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);

            if (movie.title().equals(title)) {
                movies.set(
                        i,
                        new Movie(
                                movie.id(),
                                movie.title(),
                                movie.genre(),
                                movie.year(),
                                description
                        )
                );

                return movie.title() + " was updated";
            }
        }

        return "no movie was found";
    }

    public Movie updateMovie(int id, Movie movie) {
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).id() == id) {
                Movie updatedMovie = new Movie(
                        id,
                        movie.title(),
                        movie.genre(),
                        movie.year(),
                        movie.description()
                );

                movies.set(i, updatedMovie);
                return updatedMovie;
            }
        }

        return null;
    }

    public boolean deleteMovie(int id) {
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).id() == id) {
                movies.remove(i);
                return true;
            }
        }

        return false;
    }
}
