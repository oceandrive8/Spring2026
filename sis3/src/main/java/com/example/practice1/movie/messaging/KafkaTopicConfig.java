package com.example.practice1.movie.messaging;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
class KafkaTopicConfig {

    @Bean
    NewTopic movieCreatedTopic() {
        return TopicBuilder.name(MovieEventPublisher.TOPIC).partitions(3).replicas(1).build();
    }
}
