package com.example.concert.token;

import com.example.concert.common.error.CommonException;
import com.example.concert.common.error.CommonErrorCode;
import com.example.concert.token.domain.TokenRepository;
import com.example.concert.token.domain.Token;
import com.example.concert.token.domain.TokenStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    public Token issue(Long userId) {
        LocalDateTime now = LocalDateTime.now();

        Token token = Token.builder()
                .token(UUID.randomUUID())
                .userId(userId)
                .status(TokenStatus.WAIT)
                .createdAt(now)
                .updatedAt(now)
                .build();

        this.tokenRepository.save(token);

        return token;
    }

    public Token check(String tokenString) {
        return this.tokenRepository.findByToken(UUID.fromString(tokenString))
                .orElseThrow( () -> new CommonException(CommonErrorCode.TOKEN_NOT_VALID));
    }

    public void validateActiveStatus(String tokenString) {
        Token token = this.check(tokenString);

        if (token.getStatus() != TokenStatus.ACTIVE) {
            throw new CommonException(CommonErrorCode.TOKEN_NOT_VALID);
        }
    }

    public void activateTokens(Integer amount) {
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < amount; i++) {
            Optional<Token> token = this.tokenRepository.findTopByStatusEqualsOrderByCreatedAtDesc(TokenStatus.WAIT);
            if (token.isPresent()) {
                token.get().activate(now);
                this.tokenRepository.save(token.get());
            } else {
                break;
            }
        }
    }
}
