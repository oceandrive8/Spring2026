package com.example.practice1.movie.messaging;

import com.example.practice1.movie.domain.MovieCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
class MovieCreatedConsumer {

    private static final Logger log = LoggerFactory.getLogger(MovieCreatedConsumer.class);
    private final Set<String> processed = ConcurrentHashMap.newKeySet();

    @KafkaListener(topics = MovieEventPublisher.TOPIC, groupId = "movie-audit")
    void onMovieCreated(MovieCreatedEvent event) {
        if (!processed.add(event.eventId())) {
            log.info("Duplicate event {} ignored", event.eventId());
            return;
        }
        try {
            if ("POISON".equals(event.title())) {
                throw new IllegalStateException("Cannot process movie " + event.movieId());
            }
            log.info("Processed MovieCreated: movie {} '{}' (event {})",
                    event.movieId(), event.title(), event.eventId());
        } catch (RuntimeException ex) {
            processed.remove(event.eventId());
            throw ex;
        }
    }
}
