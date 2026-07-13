package com.example.demo.namstack;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;

@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "orders") // Best practice: Avoid using SQL reserved keywords like "order"
@Data
@Entity
public class Order {

    @Id
    // Use IDENTITY strategy for MySQL to leverage the native AUTO_INCREMENT feature.
    // This resolves the "table 'order_seq' doesn't exist" error.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "age")
    private Integer age;

    @Column(name = "created_date")
    private OffsetDateTime createdDate;
}