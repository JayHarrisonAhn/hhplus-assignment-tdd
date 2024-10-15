package com.example.concert.infra.concert;

import com.example.concert.domain.ConcertTimeslot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertTimeslotRepositoryJPA extends JpaRepository<ConcertTimeslot, Long> {
}
