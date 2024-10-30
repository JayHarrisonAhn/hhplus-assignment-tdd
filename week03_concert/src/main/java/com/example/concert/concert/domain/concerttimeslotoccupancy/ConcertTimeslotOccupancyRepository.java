package com.example.concert.concert.domain.concerttimeslotoccupancy;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface ConcertTimeslotOccupancyRepository extends JpaRepository<ConcertTimeslotOccupancy, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ConcertTimeslotOccupancy> findByConcertTimeslotId(Long concertTimeslotId);
}
