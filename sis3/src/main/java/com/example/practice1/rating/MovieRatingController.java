package com.example.practice1.rating;

import com.example.practice1.movie.app.MovieService;
import com.example.practice1.movie.web.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Movies")
@RestController
@RequestMapping("/api/movie")
public class MovieRatingController {

    private final MovieService movies;
    private final RatingGateway gateway;

    public MovieRatingController(MovieService movies, RatingGateway gateway) {
        this.movies = movies;
        this.gateway = gateway;
    }

    @Operation(summary = "Get a movie's rating from the external service (with fallback)")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Rating, or fallback result (source=FALLBACK) if the service is unavailable"),
            @ApiResponse(responseCode = "404", description = "Movie not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}/rating")
    public RatingResult rating(@PathVariable Long id) {
        return gateway.ratingFor(movies.get(id).title());
    }
}
