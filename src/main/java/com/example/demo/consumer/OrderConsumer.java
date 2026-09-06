package com.example.demo.consumer;

import com.example.demo.cdc.OrderCdcEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
public class OrderConsumer {

    private final ObjectMapper objectMapper;

    public OrderConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            id = "demo-order-topic",
            topics = "mysql.mydatabase.orders",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "cdcKafkaListenerContainerFactory"
    )
    public void consume(String message) {

        OrderCdcEvent event =
                objectMapper.readValue(message, OrderCdcEvent.class);

        var payload = event.payload();

        log.info("========== ORDER CDC ==========");
        log.info("Operation: {}", payload.op());

        if ("c".equals(payload.op())) {
            log.info("CREATE: {}", payload.after());
        }

        if ("u".equals(payload.op())) {
            log.info("UPDATE:");
            log.info("Before: {}", payload.before());
            log.info("After : {}", payload.after());
        }

        if ("d".equals(payload.op())) {
            log.info("DELETE: {}", payload.before());
        }
    }
}
