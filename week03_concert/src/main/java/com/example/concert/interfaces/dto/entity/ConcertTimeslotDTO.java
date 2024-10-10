package com.example.concert.interfaces.dto.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ConcertTimeslotDTO {
    private Long id;

    private Long concertId;

    private LocalDateTime concertStartTime;

    private LocalDateTime reservationStartTime;

    private Integer maxSeatAmount;

    private Integer occupiedSeatAmount;
}
