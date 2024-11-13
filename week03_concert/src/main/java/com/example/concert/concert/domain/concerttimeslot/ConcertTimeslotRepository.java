package com.example.concert.concert.domain.concerttimeslot;

import com.example.concert.concert.dto.ConcertTimeslotWithOccupancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConcertTimeslotRepository extends JpaRepository<ConcertTimeslot, Long> {

    @Query("""
        SELECT new com.example.concert.concert.dto.ConcertTimeslotWithOccupancy(
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
        ON
            ts.concertId=:concertId
            AND ts.id=occ.concertTimeslotId
        ORDER BY
            ts.concertStartTime
    """)
    List<ConcertTimeslotWithOccupancy> findAllByConcertIdWithOccupancy(Long concertId);
}
