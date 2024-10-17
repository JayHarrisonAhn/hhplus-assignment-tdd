package com.example.concert.domain.dto;

import com.example.concert.domain.ConcertSeat;
import com.example.concert.domain.BalanceHistory;
import lombok.Builder;

@Builder
public record ConcertSeatPayInfo(
        ConcertSeat seat,
        BalanceHistory balanceHistory
) {
}
