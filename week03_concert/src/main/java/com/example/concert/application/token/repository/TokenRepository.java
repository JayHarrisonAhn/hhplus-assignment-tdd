package com.example.concert.application.token.repository;

import com.example.concert.domain.token.Token;
import com.example.concert.domain.token.TokenStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(UUID token);
    Optional<Token> findByUserId(Long userId);
    Integer countAllByStatusIs(TokenStatus status);
}
