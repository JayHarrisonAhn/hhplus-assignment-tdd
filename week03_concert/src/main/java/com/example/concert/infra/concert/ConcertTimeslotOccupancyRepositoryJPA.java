package com.example.concert.infra.concert;

import com.example.concert.domain.ConcertTimeslotOccupancy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertTimeslotOccupancyRepositoryJPA extends JpaRepository<ConcertTimeslotOccupancy, Long> {
}
