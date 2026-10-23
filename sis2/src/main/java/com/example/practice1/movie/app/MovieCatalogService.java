package com.example.practice1.movie.app;

import com.example.practice1.movie.web.MovieResponse;
import com.example.practice1.movie.infra.DirectorEntity;
import com.example.practice1.movie.infra.MovieEntity;
import com.example.practice1.movie.infra.SpringDataDirectors;
import com.example.practice1.movie.infra.SpringDataMovies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MovieCatalogService {

    private final SpringDataMovies movieRepository;
    private final SpringDataDirectors directorRepository;

    public MovieCatalogService(SpringDataMovies movieRepository,
                               SpringDataDirectors directorRepository) {
        this.movieRepository = movieRepository;
        this.directorRepository = directorRepository;
    }

    public Page<MovieResponse> list(Pageable pageable) {
        return movieRepository.findAll(pageable).map(MovieResponse::from);
    }

    public Page<MovieResponse> listByGenre(String genre, Pageable pageable) {
        return movieRepository.findByGenreIgnoreCase(genre, pageable).map(MovieResponse::from);
    }

    public Page<MovieResponse> listByDirector(String name, Pageable pageable) {
        return movieRepository.findByDirectorName(name, pageable).map(MovieResponse::from);
    }

    @Transactional
    public MovieResponse create(String title, String genre, Integer year, String description,
                                String directorName, String directorCountry) {
        DirectorEntity director = directorRepository.findByNameIgnoreCase(directorName)
                .orElseGet(() -> directorRepository.save(
                        new DirectorEntity(directorName, directorCountry)));
        MovieEntity saved = movieRepository.save(
                new MovieEntity(title, genre, year, description, director));
        return MovieResponse.from(saved);
    }
}