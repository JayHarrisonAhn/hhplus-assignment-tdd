package com.example.concert.token;

import com.example.concert.concert.event.ConcertSeatOccupyEvent;
import com.example.concert.token.domain.Token;
import com.example.concert.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Transactional
public class TokenFacade {

    private final UserService userService;
    private final TokenService tokenService;

    public Token issue(Long userId) {
        userService.findByUserId(userId);
        return this.tokenService.issue(userId);
    }

    public Token check(Long userId, String tokenString) {
        Token token = this.tokenService.check(tokenString);
        token.validateUser(userId);
        token.validateActive();
        return token;
    }

    public void refreshTokenQueue(Integer amount) {
        this.tokenService.activateTokens(amount);
    }

    public void expireToken(String tokenString) {
        this.tokenService.expireToken(tokenString);
    }
}
