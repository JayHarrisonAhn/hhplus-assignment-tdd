package com.example.concert.concert.dto;

import java.time.LocalDateTime;

public record ConcertTimeslotWithOccupancy(
        Long id,
        Long concertId,
        LocalDateTime concertStartTime,
        LocalDateTime reservationStartTime,
        Integer maxSeatAmount,
        Integer occupiedSeatAmount
) {
}
