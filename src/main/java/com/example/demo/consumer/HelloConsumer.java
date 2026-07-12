package com.example.demo.consumer;

import com.example.demo.HelloController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HelloConsumer {

    @KafkaListener(id = "demo-topic",
            topics = "demo-topic-3",
            groupId = "${spring.kafka.consumer.group-id}")
    public void sayHello(HelloController.User message) {
        log.info("Receive msg : {}", message);
    }
}
