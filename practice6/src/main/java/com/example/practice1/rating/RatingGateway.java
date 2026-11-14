package com.example.practice1.rating;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
public class RatingGateway {

    private static final Logger log = LoggerFactory.getLogger(RatingGateway.class);

    private final RatingClient client;
    private final RatingClientProperties props;

    public RatingGateway(RatingClient client, RatingClientProperties props) {
        this.client = client;
        this.props = props;
    }

    public RatingResult ratingFor(String title) {
        try {
            OmdbResponse response = client.find(title, props.apiKey());
            Double rating = parse(response);
            return rating == null ? fallback(title) : new RatingResult(title, rating, "REMOTE");
        } catch (RestClientException ex) {   // no connection, timeout, 4xx/5xx
            log.warn("Rating service failed for '{}': {}", title, ex.getMessage());
            return fallback(title);
        }
    }

    private static Double parse(OmdbResponse response) {
        if (response == null || response.imdbRating() == null) return null;
        try {
            return Double.valueOf(response.imdbRating());
        } catch (NumberFormatException e) {   // e.g. "N/A"
            return null;
        }
    }

    private static RatingResult fallback(String title) {
        return new RatingResult(title, null, "FALLBACK");
    }
}