package com.example.concert.application.token;

import com.example.concert.domain.token.Token;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Transactional
public class TokenFacade {

    private final TokenService tokenService;

    public Token issue(Long userId) {
        return this.tokenService.issue(userId);
    }

    public Token check(Long userId, String tokenString) {
        Token token = this.tokenService.check(tokenString);
        token.validateUser(userId);
        return token;
    }

    public void refreshTokenQueue(Integer amount) {
        this.tokenService.activateTokens(amount);
    }
}
