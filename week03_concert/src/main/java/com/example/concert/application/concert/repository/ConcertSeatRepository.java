package com.example.concert.application.concert.repository;

import com.example.concert.domain.ConcertSeat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface ConcertSeatRepository extends JpaRepository<ConcertSeat, Long> {

    List<ConcertSeat> findAllByConcertTimeslotIdOrderBySeatId(Long concertTimeslotId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ConcertSeat> findById(Long id);
}
