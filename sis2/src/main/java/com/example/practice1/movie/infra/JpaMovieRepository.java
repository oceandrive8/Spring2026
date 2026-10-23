package com.example.practice1.movie.infra;

import com.example.practice1.movie.domain.Director;
import com.example.practice1.movie.domain.Movie;
import com.example.practice1.movie.domain.MovieNotFoundException;
import com.example.practice1.movie.domain.MovieRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
class JpaMovieRepository implements MovieRepository {

    private final SpringDataMovies movies;
    private final SpringDataDirectors directors;
    private final MovieEntityMapper mapper;

    JpaMovieRepository(SpringDataMovies movies,
                       SpringDataDirectors directors,
                       MovieEntityMapper mapper) {
        this.movies = movies;
        this.directors = directors;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Movie> findById(Long id) {
        return movies.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByTitle(String title) {
        return movies.existsByTitleIgnoreCase(title);
    }


    @Override
    @Transactional
    public Movie save(Movie movie) {
        DirectorEntity director = resolveDirector(movie.director());

        MovieEntity entity;
        if (movie.id() == null) {
            entity = new MovieEntity(movie.title(), movie.genre(), movie.releaseYear(),
                    movie.description(), director);
        } else {
            entity = movies.findById(movie.id())
                    .orElseThrow(() -> new MovieNotFoundException(movie.id())); // adjust to your constructor
            entity.setTitle(movie.title());
            entity.setGenre(movie.genre());
            entity.setReleaseYear(movie.releaseYear());
            entity.setDescription(movie.description());
            entity.setDirector(director);
        }
        return mapper.toDomain(movies.save(entity));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        movies.deleteById(id);
    }

    private DirectorEntity resolveDirector(Director director) {
        if (director == null) return null;
        if (director.id() != null) return directors.getReferenceById(director.id());
        return directors.findByNameIgnoreCase(director.name())
                .orElseGet(() -> directors.save(new DirectorEntity(director.name(), director.country())));
    }
}
