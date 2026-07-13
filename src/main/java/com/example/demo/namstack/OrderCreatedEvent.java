package com.example.demo.namstack;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record OrderCreatedEvent(
        Long id,
        String name,
        Integer age,
        String address) {

    Order toOrder() {
        Order order = new Order();
        order.setName(name());
        order.setAge(age());
        order.setAddress(address());
        order.setCreatedDate(OffsetDateTime.now());
        return order;
    }
}