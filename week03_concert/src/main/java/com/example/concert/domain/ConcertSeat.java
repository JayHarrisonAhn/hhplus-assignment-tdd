package com.example.concert.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"concertTimeslotId", "seatId"})
})
public class ConcertSeat {

    @Id
    @GeneratedValue
    Long id;

    @Column(name = "concertTimeslotId")
    Long concertTimeslotId;

    @Column(name = "seatId")
    String seatId;

    @Nullable
    Long userId;

    @Nullable
    Long payHistoryId;

    @Nullable
    LocalDateTime occupiedAt;
}
