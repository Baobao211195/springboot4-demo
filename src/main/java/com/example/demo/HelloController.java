package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private HelloService helloService;

    @GetMapping("/hello/{message}")
    public String sayHello(@PathVariable String message) {
        return helloService.sayHello(new User(1, message, message));
    }
    public record User(Integer id, String name, String address) {
    }
}
