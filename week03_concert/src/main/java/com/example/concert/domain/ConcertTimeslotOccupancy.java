package com.example.concert.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ConcertTimeslotOccupancy {

    @Id
    private Long concertTimeslotId;

    private Integer maxSeatAmount;

    private Integer occupiedSeatAmount;
}
