package com.example.practice1.rating;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.time.Duration;
import java.util.function.Predicate;

@Configuration
public class RatingResilienceConfig {

    // Only transient failures: network problems/timeouts and 5xx.
    // 4xx (bad key, bad request) will not get better by retrying.
    private static final Predicate<Throwable> TRANSIENT = ex ->
            ex instanceof ResourceAccessException || ex instanceof HttpServerErrorException;

    @Bean
    public Retry ratingRetry() {
        return Retry.of("rating", RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(300))
                .retryOnException(TRANSIENT)
                .build());
    }

    @Bean
    public CircuitBreaker ratingCircuitBreaker() {
        return CircuitBreaker.of("rating", CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(10)
                .minimumNumberOfCalls(5)
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofSeconds(30))
                .permittedNumberOfCallsInHalfOpenState(2)
                .recordException(TRANSIENT)
                .build());
    }
}
