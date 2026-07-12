package com.example.demo;

import java.time.OffsetDateTime;

public record User(Integer id, String name, String address, OffsetDateTime createdDate) {
}
