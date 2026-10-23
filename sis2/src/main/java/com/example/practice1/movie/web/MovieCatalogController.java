package com.example.practice1.movie.web;

import com.example.practice1.movie.app.MovieService;
import com.example.practice1.movie.domain.Movie;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/catalog/movies")
public class MovieCatalogController {

    private final MovieService service;
    private final MovieApiMapper mapper;

    public MovieCatalogController(MovieService service, MovieApiMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public Page<MovieResponse> list(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String director,
            @ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        Page<Movie> page = genre != null ? service.listByGenre(genre, pageable)
                : director != null ? service.listByDirector(director, pageable)
                : service.list(pageable);
        return page.map(mapper::toResponse);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieResponse create(@Valid @RequestBody MovieRequest request) {
        return mapper.toResponse(service.create(mapper.toDomain(request)));
    }
}