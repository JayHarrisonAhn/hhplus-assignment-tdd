package com.example.concert.application.concert.repository;

import com.example.concert.application.concert.dto.ConcertTimeslotWithOccupancy;
import com.example.concert.domain.ConcertTimeslot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConcertTimeslotRepository extends JpaRepository<ConcertTimeslot, Long> {

    @Query("""
        SELECT new com.example.concert.application.concert.dto.ConcertTimeslotWithOccupancy(
            ts.id,
            ts.concertId,
            ts.concertStartTime,
            ts.reservationStartTime,
            occ.maxSeatAmount,
            occ.occupiedSeatAmount
        )
        FROM
            ConcertTimeslot ts
            INNER JOIN
            ConcertTimeslotOccupancy occ
        WHERE
            ts.concertId=:concertId
    """)
    List<ConcertTimeslotWithOccupancy> findAllByConcertIdWithOccupancy(Long concertId);
}
