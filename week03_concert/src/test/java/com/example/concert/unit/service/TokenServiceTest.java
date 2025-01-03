package com.example.concert.unit.service;

import com.example.concert.common.error.CommonException;
import com.example.concert.token.TokenService;
import com.example.concert.token.domain.TokenRepository;
import com.example.concert.token.domain.Token;
import com.example.concert.token.domain.TokenStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    TokenService tokenService;

    @Mock
    TokenRepository tokenRepository;

    @Test
    @DisplayName("토큰 발급 성공 : 해피케이스")
    void success_issue() {
        assertDoesNotThrow( () -> {
            tokenService.issue(123L);
        });
    }

    @Test
    @DisplayName("토큰 인증 성공 : 해피케이스")
    void success_validateActiveStatus() {
        // Given
        UUID tokenString = UUID.randomUUID();
        when(tokenRepository.findByToken(tokenString))
                .thenReturn(Optional.ofNullable(
                        Token.builder()
                                .token(tokenString)
                                .status(TokenStatus.ACTIVE)
                                .build()
                ));

        // When, Then
        assertDoesNotThrow(() -> {
            tokenService.validateActiveStatus(tokenString.toString());
        });
    }

    @Test
    @DisplayName("토큰 인증 실패 : WAIT일 때")
    void fail_validateActiveStatus_wait() {
        // Given
        UUID tokenString = UUID.randomUUID();
        when(tokenRepository.findByToken(tokenString))
                .thenReturn(Optional.ofNullable(
                        Token.builder()
                                .token(tokenString)
                                .status(TokenStatus.WAIT)
                                .build()
                ));

        // When, Then
        assertThrows(
                CommonException.class, () -> {
                    tokenService.validateActiveStatus(tokenString.toString());
                }
        );
    }

    @Test
    @DisplayName("토큰 인증 실패 : EXPIRED일 때")
    void fail_validateActiveStatus_expired() {
        // Given
        UUID tokenString = UUID.randomUUID();
        when(tokenRepository.findByToken(tokenString))
                .thenReturn(Optional.ofNullable(
                        Token.builder()
                                .token(tokenString)
                                .status(TokenStatus.EXPIRED)
                                .build()
                ));

        // When, Then
        assertThrows(
                CommonException.class, () -> {
                    tokenService.validateActiveStatus(tokenString.toString());
                }
        );
    }
}