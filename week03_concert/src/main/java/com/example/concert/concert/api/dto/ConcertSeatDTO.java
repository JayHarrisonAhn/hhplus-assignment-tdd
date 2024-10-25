package com.example.concert.concert.api.dto;

import com.example.concert.concert.domain.concertseat.ConcertSeat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConcertSeatDTO {

    Long id;
    Long concertTimeslotId;
    String seatId;
    Boolean isEmpty;

    public static ConcertSeatDTO from(ConcertSeat concertSeat) {
        return ConcertSeatDTO.builder()
                .id(concertSeat.getId())
                .concertTimeslotId(concertSeat.getConcertTimeslotId())
                .seatId(concertSeat.getSeatId())
                .isEmpty(concertSeat.getOccupiedAt() == null)
                .build();
    }
}
