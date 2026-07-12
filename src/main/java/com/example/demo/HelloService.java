package com.example.demo;

import com.example.demo.consumer.KafkaProducerService;
import org.springframework.stereotype.Service;

@Service
public class HelloService {
    private final KafkaProducerService kafkaProducerService;

    public HelloService(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    public String sayHello(HelloController.User message) {
        kafkaProducerService.sendMessage(message);
        return message.name();
    }
}
