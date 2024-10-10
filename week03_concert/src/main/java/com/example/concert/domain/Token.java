package com.example.concert.domain;

import com.example.concert.domain.enums.TokenStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Token {

    @Id
    private String token;

    private String userId;

    @Enumerated(EnumType.STRING)
    private TokenStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
