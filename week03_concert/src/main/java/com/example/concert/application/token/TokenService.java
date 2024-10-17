package com.example.concert.application.token;

import com.example.concert.application.token.repository.TokenRepository;
import com.example.concert.domain.Token;
import com.example.concert.domain.enums.TokenStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    Token issue(Long userId) {
        LocalDateTime now = LocalDateTime.now();

        Token token = Token.builder()
                .userId(userId)
                .status(TokenStatus.WAIT)
                .createdAt(now)
                .updatedAt(now)
                .build();

        this.tokenRepository.save(token);

        return token;
    }

    Token check(Long userId, UUID tokenString) {
        Token token = this.tokenRepository.findByToken(tokenString)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Token not found"));

        if (!token.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Token not found");
        }
        
        return token;
    }
}
