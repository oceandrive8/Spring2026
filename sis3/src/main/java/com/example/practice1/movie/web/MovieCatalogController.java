package com.example.practice1.movie.web;

import com.example.practice1.movie.app.MovieService;
import com.example.practice1.movie.domain.Movie;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Movie Catalog", description = "Browsing and filtering the catalog")
@RestController
@RequestMapping("/api/catalog/movies")
public class MovieCatalogController {

    private final MovieService service;
    private final MovieApiMapper mapper;

    public MovieCatalogController(MovieService service, MovieApiMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(summary = "List movies, optionally filtered by genre or director",
            description = "If both filters are given, genre takes precedence.")
    @ApiResponse(responseCode = "200", description = "Page of movies")
    @GetMapping
    public Page<MovieResponse> list(
            @Parameter(description = "Filter by genre", example = "Fantasy")
            @RequestParam(required = false) String genre,
            @Parameter(description = "Filter by director name")
            @RequestParam(required = false) String director,
            @ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        Page<Movie> page = genre != null ? service.listByGenre(genre, pageable)
                : director != null ? service.listByDirector(director, pageable)
                : service.list(pageable);
        return page.map(mapper::toResponse);
    }

    @Operation(summary = "Create a movie")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Movie created"),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "A movie with this title already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Domain rule violated (e.g. blank title, invalid year)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieResponse create(@Valid @RequestBody MovieRequest request) {
        return mapper.toResponse(service.create(mapper.toDomain(request)));
    }
}