package com.example.concert.concert.domain.concertseat;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConcertSeatRepository extends JpaRepository<ConcertSeat, Long> {

    List<ConcertSeat> findAllByConcertTimeslotIdOrderBySeatId(Long concertTimeslotId);

    Optional<ConcertSeat> findById(Long id);
}
