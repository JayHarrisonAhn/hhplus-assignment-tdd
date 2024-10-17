package com.example.concert.application.token;

import com.example.concert.application.token.repository.TokenRepository;
import com.example.concert.domain.token.Token;
import com.example.concert.domain.token.TokenStatus;
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
    void success_issue() {
        assertDoesNotThrow( () -> {
            tokenService.issue(123L);
        });
    }

    @Test
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