package com.example.concert.application.concert.repository;

import com.example.concert.domain.ConcertTimeslot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertTimeslotRepository extends JpaRepository<ConcertTimeslot, Long> {
}
