package com.example.concert.domain.dto;

import com.example.concert.domain.ConcertSeat;
import com.example.concert.domain.PayHistory;
import lombok.Builder;

@Builder
public record ConcertSeatPayInfo(
        ConcertSeat seat,
        PayHistory payHistory
) {
}
