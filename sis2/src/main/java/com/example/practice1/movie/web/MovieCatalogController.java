package com.example.practice1.movie.web;

import com.example.practice1.movie.app.MovieCatalogService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/catalog/movies")
public class MovieCatalogController {

    private final MovieCatalogService service;

    public MovieCatalogController(MovieCatalogService service) {
        this.service = service;
    }

    @GetMapping
    public Page<MovieResponse> list(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String director,
            @ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        if (genre != null) return service.listByGenre(genre, pageable);
        if (director != null) return service.listByDirector(director, pageable);
        return service.list(pageable);
    }

    public record CreateMovieRequest(String title, String genre, Integer releaseYear,
                                     String description,
                                     String directorName, String directorCountry) {}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieResponse create(@RequestBody CreateMovieRequest r) {
        return service.create(r.title(), r.genre(), r.releaseYear(), r.description(),
                r.directorName(), r.directorCountry());
    }
}