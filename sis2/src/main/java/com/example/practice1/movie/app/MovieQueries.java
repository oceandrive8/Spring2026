package com.example.practice1.movie.app;

import com.example.practice1.movie.domain.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieQueries {
    Page<Movie> list(Pageable pageable);
    Page<Movie> listByGenre(String genre, Pageable pageable);
    Page<Movie> listByDirector(String directorName, Pageable pageable);
}
