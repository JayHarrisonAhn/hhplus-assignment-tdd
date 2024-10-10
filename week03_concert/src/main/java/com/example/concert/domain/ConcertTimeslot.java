package com.example.concert.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class ConcertTimeslot {

    @Id
    @GeneratedValue
    private Long id;

    private Long concertId;

    private LocalDateTime concertStartTime;

    private LocalDateTime reservationStartTime;
}
