package com.example.demo.stream;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderStateStoreService stateStoreService;

    public OrderController(
            OrderStateStoreService stateStoreService) {

        this.stateStoreService = stateStoreService;
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderHistory> getOrder(
            @PathVariable Integer orderId) {

        OrderHistory order =
                stateStoreService.get(orderId);

        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(order);
    }
}