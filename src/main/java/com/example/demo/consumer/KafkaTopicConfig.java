package com.example.demo.consumer;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
@EnableKafka
@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic demoTopic() {
        return TopicBuilder.name("demo-topic-8").partitions(3).replicas(1).build();
    }
}
