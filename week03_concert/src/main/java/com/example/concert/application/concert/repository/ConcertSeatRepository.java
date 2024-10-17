package com.example.concert.application.concert.repository;

import com.example.concert.domain.ConcertSeat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertSeatRepository extends JpaRepository<ConcertSeat, Long> {
}
