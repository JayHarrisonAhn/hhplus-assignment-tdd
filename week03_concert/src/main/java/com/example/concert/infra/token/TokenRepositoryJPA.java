package com.example.concert.infra.token;

import com.example.concert.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TokenRepositoryJPA extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(UUID token);
    Optional<Token> findByUserId(Long userId);
}
