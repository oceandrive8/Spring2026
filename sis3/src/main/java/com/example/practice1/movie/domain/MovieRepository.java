package com.example.practice1.movie.domain;

import java.util.List;
import java.util.Optional;

public interface MovieRepository {
    Optional<Movie> findById(Long id);
    boolean existsByTitle(String title);
    Movie save(Movie movie);
    void deleteById(Long id);
}