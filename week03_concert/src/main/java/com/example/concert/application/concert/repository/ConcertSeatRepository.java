package com.example.concert.application.concert.repository;

import com.example.concert.domain.ConcertSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConcertSeatRepository extends JpaRepository<ConcertSeat, Long> {

    List<ConcertSeat> findAllByConcertTimeslotIdOrderBySeatId(Long concertTimeslotId);

    Optional<ConcertSeat> findAllBySeatId(String seatId);
}
