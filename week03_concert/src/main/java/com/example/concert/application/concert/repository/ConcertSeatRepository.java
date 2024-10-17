package com.example.concert.application.concert.repository;

import com.example.concert.domain.ConcertSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcertSeatRepository extends JpaRepository<ConcertSeat, Long> {

    List<ConcertSeat> findAllByConcertTimeslotIdOrderBySeatId(Long concertTimeslotId);
}
