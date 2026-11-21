package com.example.practice1.movie.app;

import com.example.practice1.movie.domain.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
public class MovieService {

    private final MovieRepository repository;
    private final MovieQueries queries;
    private final ApplicationEventPublisher events;

    public MovieService(MovieRepository repository, MovieQueries queries, ApplicationEventPublisher events) {
        this.repository = repository;
        this.queries = queries;
        this.events = events;
    }
    @Transactional
    public Movie create(Movie movie) {
        if (repository.existsByTitle(movie.title())) {
            throw new MovieConflictException(
                    "A movie with title '" + movie.title() + "' already exists");
        }
        Movie saved = repository.save(movie);
        // Delivered to Kafka only after this transaction commits
        events.publishEvent(new MovieCreatedEvent(
                UUID.randomUUID().toString(), saved.id(), saved.title(), Instant.now()));
        return saved;
    }

    public Page<Movie> list(Pageable p) { return queries.list(p); }
    public Page<Movie> listByGenre(String genre, Pageable p) { return queries.listByGenre(genre, p); }
    public Page<Movie> listByDirector(String name, Pageable p) { return queries.listByDirector(name, p); }

    @Cacheable(cacheNames = "movies", key = "#id")
    public Movie get(Long id) {
        return repository.findById(id).orElseThrow(() -> new MovieNotFoundException(id));
    }


    @CachePut(cacheNames = "movies", key = "#id")
    public Movie update(Long id, Movie changes) {
        Movie existing = get(id);
        Movie updated = new Movie(id, changes.title(), changes.genre(), changes.releaseYear(),
                changes.description(), existing.director());
        return repository.save(updated);
    }

    @CacheEvict(cacheNames = "movies", key = "#id")
    public void delete(Long id) {
        get(id);
        repository.deleteById(id);
    }
}
