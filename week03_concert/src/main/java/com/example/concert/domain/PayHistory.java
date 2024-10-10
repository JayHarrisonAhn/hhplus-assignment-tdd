package com.example.concert.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class PayHistory {

    @Id
    @GeneratedValue
    private Long id;

    private Long userId;

    private Long amount;

    private LocalDateTime createdAt;
}
