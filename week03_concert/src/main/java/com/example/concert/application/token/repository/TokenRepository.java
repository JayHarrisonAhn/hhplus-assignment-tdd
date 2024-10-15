package com.example.concert.application.token.repository;

import com.example.concert.domain.Token;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public interface TokenRepository {

    public Token save(Token token);
    public Optional<Token> findByToken(UUID tokenString);
    public Optional<Token> findByUserId(Long userId);
}
