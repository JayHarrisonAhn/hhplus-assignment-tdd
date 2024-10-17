package com.example.concert.application.token;

import com.example.concert.application.token.repository.TokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

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
}