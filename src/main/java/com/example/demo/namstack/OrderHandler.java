package com.example.demo.namstack;

import io.namastack.outbox.annotation.OutboxHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderHandler {

    @OutboxHandler
    public void handleOrder(OrderCreatedEvent payload) {
        log.info("Received OrderCreatedEvent: {} ", payload);
    }
}