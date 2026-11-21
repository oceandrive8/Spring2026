package com.example.practice1.rating;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.function.Supplier;

@Service
public class RatingGateway {

    private static final Logger log = LoggerFactory.getLogger(RatingGateway.class);

    private final RatingClient client;
    private final RatingClientProperties props;
    private final Retry retry;
    private final CircuitBreaker circuitBreaker;

    public RatingGateway(RatingClient client, RatingClientProperties props,
                         Retry ratingRetry, CircuitBreaker ratingCircuitBreaker) {
        this.client = client;
        this.props = props;
        this.retry = ratingRetry;
        this.circuitBreaker = ratingCircuitBreaker;
    }

    public RatingResult ratingFor(String title) {
        Supplier<OmdbResponse> call = () -> client.find(title, props.apiKey());
        Supplier<OmdbResponse> resilient =
                Retry.decorateSupplier(retry, CircuitBreaker.decorateSupplier(circuitBreaker, call));
        try {
            Double rating = parse(resilient.get());
            return rating == null ? fallback(title) : new RatingResult(title, rating, "REMOTE");
        } catch (CallNotPermittedException ex) {
            log.warn("Rating circuit is OPEN, using fallback for '{}'", title);
            return fallback(title);
        } catch (RestClientException ex) {
            log.warn("Rating service failed for '{}': {}", title, ex.getMessage());
            return fallback(title);
        }
    }

    private static Double parse(OmdbResponse response) {
        if (response == null || response.imdbRating() == null) return null;
        try {
            return Double.valueOf(response.imdbRating());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static RatingResult fallback(String title) {
        return new RatingResult(title, null, "FALLBACK");
    }
}