package com.example.concert.domain;

import com.example.concert.domain.enums.TokenStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID token;

    @Column(unique = true)
    private Long userId;

    @Enumerated(EnumType.STRING)
    private TokenStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
