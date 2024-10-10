package com.example.concert.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Concert {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
}
