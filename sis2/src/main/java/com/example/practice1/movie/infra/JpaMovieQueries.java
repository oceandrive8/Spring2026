package com.example.practice1.movie.infra;

import com.example.practice1.movie.app.MovieQueries;
import com.example.practice1.movie.domain.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
class JpaMovieQueries implements MovieQueries {

    private final SpringDataMovies movies;
    private final MovieEntityMapper mapper;

    JpaMovieQueries(SpringDataMovies movies, MovieEntityMapper mapper) {
        this.movies = movies;
        this.mapper = mapper;
    }

    @Override public Page<Movie> list(Pageable p) { return movies.findAll(p).map(mapper::toDomain); }
    @Override public Page<Movie> listByGenre(String g, Pageable p) { return movies.findByGenreIgnoreCase(g, p).map(mapper::toDomain); }
    @Override public Page<Movie> listByDirector(String n, Pageable p) { return movies.findByDirectorName(n, p).map(mapper::toDomain); }
}
