package com.example.practice1.movie.app;

import com.example.practice1.movie.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    private final MovieRepository repository;
    private final MovieQueries queries;

    public MovieService(MovieRepository repository, MovieQueries queries) {
        this.repository = repository;
        this.queries = queries;
    }

    public Page<Movie> list(Pageable p) { return queries.list(p); }
    public Page<Movie> listByGenre(String genre, Pageable p) { return queries.listByGenre(genre, p); }
    public Page<Movie> listByDirector(String name, Pageable p) { return queries.listByDirector(name, p); }

    public Movie get(Long id) {
        return repository.findById(id).orElseThrow(() -> new MovieNotFoundException(id));
    }

    public Movie create(Movie movie) {
        if (repository.existsByTitle(movie.title())) {
            throw new MovieConflictException(
                    "A movie with title '" + movie.title() + "' already exists");
        }
        return repository.save(movie);
    }

    public Movie update(Long id, Movie changes) {
        Movie existing = get(id);
        Movie updated = new Movie(id, changes.title(), changes.genre(), changes.releaseYear(),
                changes.description(), existing.director()); // keep the current director
        return repository.save(updated);
    }

    public void delete(Long id) {
        get(id); // throws MovieNotFoundException if missing
        repository.deleteById(id);
    }
}
