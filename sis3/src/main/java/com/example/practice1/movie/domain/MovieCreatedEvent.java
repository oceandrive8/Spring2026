package com.example.practice1.movie.domain;

import java.time.Instant;

// Named in the past tense: an event is a fact that already happened.
public record MovieCreatedEvent(String eventId, Long movieId, String title, Instant occurredAt) { }
