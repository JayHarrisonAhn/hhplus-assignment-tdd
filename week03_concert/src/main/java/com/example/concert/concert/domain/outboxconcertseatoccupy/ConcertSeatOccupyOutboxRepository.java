package com.example.concert.concert.domain.outboxconcertseatoccupy;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.time.LocalDateTime;
import java.util.List;

public interface ConcertSeatOccupyOutboxRepository extends JpaRepository<ConcertSeatOccupyOutbox, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public List<ConcertSeatOccupyOutbox> findTop50ByEventStatusEqualsAndCreatedAtBeforeOrderByIdAsc(ConcertSeatOccupyOutbox.EventStatus eventStatus, LocalDateTime createdAt);
}
