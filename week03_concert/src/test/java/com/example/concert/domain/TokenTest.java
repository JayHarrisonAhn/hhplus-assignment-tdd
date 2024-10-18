package com.example.concert.domain;

import com.example.concert.domain.token.Token;
import com.example.concert.domain.token.TokenStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TokenTest {

    Token token;
    Long userId;

    @BeforeEach
    void setUp() {
        this.userId = 1L;
        this.token = Token.builder()
                .token(UUID.randomUUID())
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .status(TokenStatus.WAIT)
                .build();
    }

    @Test
    @DisplayName("토큰 확인 성공 : 해피케이스")
    void deductBalance_success() {
        assertDoesNotThrow( () -> {
            this.token.validateUser(this.userId);
        });
    }

    @Test
    @DisplayName("토큰 확인 실패 : 유저가 다름")
    void deductBalance_fail_minusBalance() {
        assertThrows(
                ResponseStatusException.class, () -> {
                    this.token.validateUser(2L);
                }
        );
    }
}