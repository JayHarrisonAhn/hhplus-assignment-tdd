package com.example.concert.infra.token;

import com.example.concert.application.token.repository.TokenRepository;
import com.example.concert.domain.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Primary
@Component
@RequiredArgsConstructor
public class TokenRepositoryImpl implements TokenRepository {

    private final TokenRepositoryJPA tokenRepository;

    @Override
    public Token save(Token token) {
        return tokenRepository.save(token);
    }

    @Override
    public Optional<Token> findByToken(UUID tokenString) {
        return tokenRepository.findByToken(tokenString);
    }

    @Override
    public Optional<Token> findByUserId(Long userId) {
        return tokenRepository.findByUserId(userId);
    }
}
