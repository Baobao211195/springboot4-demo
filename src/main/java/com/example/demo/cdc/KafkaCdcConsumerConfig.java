package com.example.demo.cdc;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaCdcConsumerConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> cdcKafkaListenerContainerFactory(
            ConsumerFactory<String, String> cdcConsumerFactory) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(cdcConsumerFactory);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> cdcConsumerFactory(KafkaProperties kafkaProperties) {

        var props = new HashMap<>(kafkaProperties.buildConsumerProperties());
        props.put(
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
            StringDeserializer.class
        );
        props.put(
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            StringDeserializer.class
        );
        return new DefaultKafkaConsumerFactory<>(props);
    }
}
