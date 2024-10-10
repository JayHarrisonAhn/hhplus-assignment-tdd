package com.example.concert.interfaces.dto.entity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ConcertSeatDTO {

    Long id;
    Long concertTimeslotId;
    String seatId;
    Boolean isEmpty;
}
