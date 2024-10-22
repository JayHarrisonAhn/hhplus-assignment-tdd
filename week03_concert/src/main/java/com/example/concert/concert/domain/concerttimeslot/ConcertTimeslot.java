package com.example.concert.concert.domain.concerttimeslot;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcertTimeslot {

    @Id
    @GeneratedValue
    private Long id;

    private Long concertId;

    private LocalDateTime concertStartTime;

    private LocalDateTime reservationStartTime;
}
