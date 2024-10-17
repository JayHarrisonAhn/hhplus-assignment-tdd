package com.example.concert.application.concert.repository;

import com.example.concert.domain.ConcertTimeslotOccupancy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertTimeslotOccupancyRepository extends JpaRepository<ConcertTimeslotOccupancy, Long> {

    ConcertTimeslotOccupancy findByConcertTimeslotId(Long concertTimeslotId);
}
