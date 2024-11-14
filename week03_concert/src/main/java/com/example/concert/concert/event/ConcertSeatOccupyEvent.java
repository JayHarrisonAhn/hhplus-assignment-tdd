package com.example.concert.concert.event;

import java.util.Optional;

public record ConcertSeatOccupyEvent(
        Optional<String> tokenString
) {
}
