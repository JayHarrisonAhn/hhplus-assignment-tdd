package com.example.concert.concert.domain.concerttimeslot;

import jakarta.persistence.*;
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
@Table(
        indexes = @Index(
                name = "idx_concert_id",
                columnList = "concertId, reservationStartTime"
        )
)
public class ConcertTimeslot {

    @Id
    @GeneratedValue
    private Long id;

    private Long concertId;

    private LocalDateTime concertStartTime;

    private LocalDateTime reservationStartTime;
}
