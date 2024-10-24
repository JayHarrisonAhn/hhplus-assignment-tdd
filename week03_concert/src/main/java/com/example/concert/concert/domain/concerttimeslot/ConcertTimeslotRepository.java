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
        WHERE
            ts.concertId=:concertId
    """)
    List<ConcertTimeslotWithOccupancy> findAllByConcertIdWithOccupancy(Long concertId);
}
