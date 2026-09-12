package com.example.demo.stream;

public record DebeziumEvent(
        Order before,
        Order after,
        String op
) {
}