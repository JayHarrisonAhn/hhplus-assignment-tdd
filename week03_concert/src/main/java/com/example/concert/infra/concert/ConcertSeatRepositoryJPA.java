package com.example.concert.infra.concert;

import com.example.concert.domain.ConcertSeat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertSeatRepositoryJPA extends JpaRepository<ConcertSeat, Long> {
}
