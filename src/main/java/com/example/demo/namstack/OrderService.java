package com.example.demo.namstack;

import io.namastack.outbox.Outbox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class OrderService {

    private final Outbox outbox;
    private final OrderRepository orderRepository;

    public OrderService(Outbox outbox, OrderRepository orderRepository) {
        this.outbox = outbox;
        this.orderRepository = orderRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public void createOrder(OrderCreatedEvent command) {
        Order order = command.toOrder();
        log.info("Saving order: {}", order);
        orderRepository.save(order);

        // Schedule event - saved atomically with the order
//        outbox.schedule(
//            new OrderCreatedEvent(order.getId(), order.getName(), order.getAge(), order.getAddress()),
//            "order-" + order.getId()  // Groups records for ordered processing
//        );
    }
}