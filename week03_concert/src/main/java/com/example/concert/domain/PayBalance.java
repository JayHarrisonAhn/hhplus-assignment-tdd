package com.example.concert.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class PayBalance {

    @Id
    @GeneratedValue
    private Long id;

    private Long userId;

    private Long balance;
}
