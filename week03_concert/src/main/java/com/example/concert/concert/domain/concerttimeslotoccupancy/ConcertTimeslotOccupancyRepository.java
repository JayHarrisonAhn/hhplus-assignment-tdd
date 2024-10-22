package com.example.concert.concert.domain.concerttimeslotoccupancy;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertTimeslotOccupancyRepository extends JpaRepository<ConcertTimeslotOccupancy, Long> {

    ConcertTimeslotOccupancy findByConcertTimeslotId(Long concertTimeslotId);
}
