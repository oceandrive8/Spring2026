package com.example.practice1.movie.web;

import com.example.practice1.movie.app.MovieService;
import com.example.practice1.movie.domain.Movie;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@RequestMapping("/api/movie")
public class MovieRecController {

    private final MovieService service;
    private final MovieApiMapper mapper;

    public MovieRecController(MovieService service, MovieApiMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public Page<MovieResponse> list(@ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return service.list(pageable).map(mapper::toResponse);
    }

    @GetMapping("/{id}")
    public MovieResponse get(@PathVariable Long id) {
        return mapper.toResponse(service.get(id));
    }

    @PostMapping
    public ResponseEntity<MovieResponse> create(@Valid @RequestBody MovieRequest request) {
        Movie created = service.create(mapper.toDomain(request));
        return ResponseEntity.created(URI.create("/api/movie/" + created.id()))
                .body(mapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public MovieResponse update(@PathVariable Long id, @Valid @RequestBody MovieUpdateRequest request) {
        return mapper.toResponse(service.update(id, mapper.toDomain(request)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
