package com.example.concert.interfaces.dto.entity;

import com.example.concert.application.concert.dto.ConcertTimeslotWithOccupancy;
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

    public static ConcertTimeslotDTO from(ConcertTimeslotWithOccupancy timeslot) {
        return ConcertTimeslotDTO.builder()
                .id(timeslot.id())
                .concertId(timeslot.concertId())
                .concertStartTime(timeslot.concertStartTime())
                .maxSeatAmount(timeslot.maxSeatAmount())
                .occupiedSeatAmount(timeslot.occupiedSeatAmount())
                .build();
    }
}
