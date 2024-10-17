package com.example.concert.application.token;

import com.example.concert.application.token.repository.TokenRepository;
import com.example.concert.domain.token.Token;
import com.example.concert.domain.token.TokenStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
    void fail_validateActiveStatus() {
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
                IllegalStateException.class, () -> {
                    tokenService.validateActiveStatus(tokenString.toString());
                }
        );
    }
}