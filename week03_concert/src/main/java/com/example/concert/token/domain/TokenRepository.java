package com.example.concert.token.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

//public interface TokenRepository extends JpaRepository<Token, Long> {
public interface TokenRepository {
    Token save(Token token);
    Optional<Token> findByToken(UUID token);
    Optional<Token> findTopByStatusEqualsOrderByCreatedAtDesc(TokenStatus status);
}
