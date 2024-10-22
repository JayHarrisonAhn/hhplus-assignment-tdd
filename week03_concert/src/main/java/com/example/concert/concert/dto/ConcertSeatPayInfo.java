package com.example.concert.concert.dto;

import com.example.concert.concert.domain.concertseat.ConcertSeat;
import com.example.concert.balance.domain.balancehistory.BalanceHistory;
import lombok.Builder;

@Builder
public record ConcertSeatPayInfo(
        ConcertSeat seat,
        BalanceHistory balanceHistory
) {
}
