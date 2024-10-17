package com.example.concert.application.token.repository;

import com.example.concert.domain.token.Token;
import com.example.concert.domain.token.TokenStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(UUID token);
    Optional<Token> findByUserId(Long userId);
    Integer countAllByStatusIs(TokenStatus status);
    Optional<Token> findTopByStatusEqualsOrderByCreatedAtDesc(TokenStatus status);

    @Query("""
        UPDATE Token t
        SET t.status="EXPIRED"
        WHERE :dtime < CURRENT_TIMESTAMP
    """)
    void updateAllByCreatedAtBeforeToExpired(@Param("dtime") LocalDateTime decisionTime);
}
