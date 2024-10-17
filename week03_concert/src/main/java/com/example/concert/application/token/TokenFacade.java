package com.example.concert.application.token;

import com.example.concert.domain.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TokenFacade {

    private final TokenService tokenService;

    public Token issue(Long userId) {
        return this.tokenService.issue(userId);
    }

    public Token check(Long userId, UUID tokenString) {
        return this.tokenService.check(userId, tokenString);
    }
}
