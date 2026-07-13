package com.example.demo;

import com.example.demo.namstack.Order;
import com.example.demo.namstack.OrderCreatedEvent;
import com.example.demo.namstack.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    @Autowired
    private HelloService helloService;

    private final OrderService orderService;

    public HelloController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/hello/{message}")
    public String sayHello(@PathVariable String message) {
        return helloService.sayHello(new User(1, message, message));
    }
    public record User(Integer id, String name, String address) {
    }

    @PostMapping("/order")
    public String order(@RequestBody OrderCreatedEvent order) {
         orderService.createOrder(order);
         return order.name();
    }
}
