package com.example.concert.interfaces.controller;

import com.example.concert.interfaces.dto.ConcertControllerDTO.*;
import com.example.concert.interfaces.dto.entity.ConcertSeatDTO;
import com.example.concert.interfaces.dto.entity.ConcertTimeslotDTO;
import com.example.concert.interfaces.dto.entity.PayHistoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/concert")
public class ConcertController {

    @GetMapping("/{concertId}/timeSlot")
    GetAvailableTimeslots.Response getAvailableTimeslots(
            GetAvailableTimeslots.Request request,
            @PathVariable("concertId") Long concertId
    ) {
        List<ConcertTimeslotDTO> dummyTimeslots = new ArrayList<>();
        dummyTimeslots.add(
                ConcertTimeslotDTO.builder()
                        .id(1L)
                        .concertId(concertId)
                        .concertStartTime(LocalDateTime.of(2024, 12, 1, 12, 0))
                        .reservationStartTime(LocalDateTime.of(2024, 11, 1, 12, 0))
                        .build()
        );
        return GetAvailableTimeslots.Response.builder()
                .timeSlots(dummyTimeslots)
                .build();
    }

    @GetMapping("/{concertId}/timeSlot/{timeSlotId}/seat")
    GetAvailableSeats.Response getAvailableSeats(
            GetAvailableSeats.Request request,
            @PathVariable("concertId") Long concertId,
            @PathVariable("timeSlotId") Long timeSlotId
    ) {
        List<ConcertSeatDTO> dummySeats = new ArrayList<>();
        dummySeats.add(
                ConcertSeatDTO.builder()
                        .id(1L)
                        .concertTimeslotId(timeSlotId)
                        .seatId("5D")
                        .isEmpty(Boolean.TRUE)
                        .build()
        );
        return GetAvailableSeats.Response.builder()
                .seats(dummySeats)
                .build();
    }

    @PostMapping("/{concertId}/timeSlot/{timeSlotId}/seat/{seatId}")
    OccupySeat.Response occupySeat(
            OccupySeat.Request request,
            @PathVariable("concertId") Long concertId,
            @PathVariable("timeSlotId") Long timeSlotId,
            @PathVariable("seatId") String seatId
    ) {
        return OccupySeat.Response.builder()
                .seat(
                        ConcertSeatDTO.builder()
                                .id(1L)
                                .concertTimeslotId(timeSlotId)
                                .seatId(seatId)
                                .isEmpty(Boolean.FALSE)
                                .build()
                ).build();
    }

    @PostMapping("/{concertId}/timeSlot/{timeSlotId}/seat/{seatId}/pay")
    PayReservation.Response payReservation(
            PayReservation.Request request,
            @PathVariable("concertId") Long concertId,
            @PathVariable("timeSlotId") Long timeSlotId,
            @PathVariable("seatId") String seatId
    ) {
        return PayReservation.Response.builder()
                .seat(
                        ConcertSeatDTO.builder()
                                .id(1L)
                                .concertTimeslotId(timeSlotId)
                                .seatId(seatId)
                                .isEmpty(Boolean.FALSE)
                                .build()
                )
                .payHistory(
                        PayHistoryDTO.builder()
                                .id(1L)
                                .userId(request.getUserId())
                                .amount(1000L)
                                .createdAt(LocalDateTime.now())
                                .build()
                )
                .build();
    }
}
