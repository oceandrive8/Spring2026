package com.example.practice1.movie.messaging;

import com.example.practice1.movie.domain.MovieCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
class MovieEventPublisher {

    static final String TOPIC = "movie-created";
    private static final Logger log = LoggerFactory.getLogger(MovieEventPublisher.class);

    private final KafkaTemplate<String, MovieCreatedEvent> kafka;

    MovieEventPublisher(KafkaTemplate<String, MovieCreatedEvent> kafka) {
        this.kafka = kafka;
    }

    // Runs only after the DB transaction has committed
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void onMovieCreated(MovieCreatedEvent event) {
        try {
            // key = aggregate id: all events of one movie go to the same partition, in order
            kafka.send(TOPIC, String.valueOf(event.movieId()), event)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to publish {}", event, ex);
                        } else {
                            log.info("Published {} to partition {} offset {}", event.eventId(),
                                    result.getRecordMetadata().partition(),
                                    result.getRecordMetadata().offset());
                        }
                    });
        } catch (Exception ex) {
            log.error("Could not publish {}", event, ex);
        }
    }
}