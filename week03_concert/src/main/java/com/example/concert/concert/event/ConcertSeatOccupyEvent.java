package com.example.concert.concert.event;

import java.util.Optional;

public record ConcertSeatOccupyEvent(
        Long seatId,
        Long userId,
        Optional<String> tokenString
) {
}
