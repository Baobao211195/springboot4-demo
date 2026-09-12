package com.example.demo.stream;

import java.time.OffsetDateTime;

public record Order(
        Integer id,
        String name,
        String address,
        Integer age,
        OffsetDateTime createdDate
) {
}