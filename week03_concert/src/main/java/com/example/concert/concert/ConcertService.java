package com.example.concert.concert;

import com.example.concert.common.error.CommonErrorCode;
import com.example.concert.common.error.CommonException;
import com.example.concert.concert.domain.concert.Concert;
import com.example.concert.concert.domain.concertseat.ConcertSeat;
import com.example.concert.concert.domain.concerttimeslot.ConcertTimeslot;
import com.example.concert.concert.domain.concert.ConcertRepository;
import com.example.concert.concert.domain.concertseat.ConcertSeatRepository;
import com.example.concert.concert.domain.concerttimeslot.ConcertTimeslotRepository;
import com.example.concert.concert.domain.concerttimeslotoccupancy.ConcertTimeslotOccupancy;
import com.example.concert.concert.domain.concerttimeslotoccupancy.ConcertTimeslotOccupancyRepository;
import com.example.concert.concert.domain.outboxconcertseatoccupy.ConcertSeatOccupyOutbox;
import com.example.concert.concert.domain.outboxconcertseatoccupy.ConcertSeatOccupyOutboxRepository;
import com.example.concert.concert.dto.ConcertTimeslotWithOccupancy;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final ConcertTimeslotRepository concertTimeslotRepository;
    private final ConcertSeatRepository concertSeatRepository;
    private final ConcertTimeslotOccupancyRepository concertTimeslotOccupancyRepository;
    private final ConcertSeatOccupyOutboxRepository concertSeatOccupyOutboxRepository;

    private final ObjectMapper objectMapper;

    Concert createConcert(String name) {
        Concert concert = Concert.builder()
                .name(name)
                .build();
        concertRepository.save(concert);
        return concert;
    }

    Concert findConcert(Long concertId) {
        return concertRepository.findById(concertId)
                .orElseThrow(() -> new CommonException(CommonErrorCode.CONCERT_NOT_FOUND));
    }

    ConcertTimeslot createConcertTimeslot(Long concertId, LocalDateTime concertStartTime, LocalDateTime reservationStartTime) {
        ConcertTimeslot concertTimeslot = ConcertTimeslot.builder()
                .concertId(concertId)
                .concertStartTime(concertStartTime)
                .reservationStartTime(reservationStartTime)
                .build();
        concertTimeslotRepository.save(concertTimeslot);

        ConcertTimeslotOccupancy concertTimeslotOccupancy = ConcertTimeslotOccupancy.builder()
                .concertTimeslotId(concertTimeslot.getId())
                .maxSeatAmount(0)
                .occupiedSeatAmount(0)
                .build();
        concertTimeslotOccupancyRepository.save(concertTimeslotOccupancy);

        return concertTimeslot;
    }

    List<ConcertTimeslotWithOccupancy> findConcertTimeslots(Long concertId) {
        return this.concertTimeslotRepository.findAllByConcertIdWithOccupancy(concertId);
    }

    ConcertTimeslot findConcertTimeslot(Long timeslotId) {
        return this.concertTimeslotRepository.findById(timeslotId)
                .orElseThrow(() -> new CommonException(CommonErrorCode.CONCERT_TIMESLOT_NOT_FOUND));
    }

    ConcertTimeslotOccupancy findConcertTimeslotOccupancy(Long timeslotId) {
        return this.concertTimeslotOccupancyRepository.findByConcertTimeslotId(timeslotId)
                .orElseThrow(() -> new CommonException(CommonErrorCode.CONCERT_TIMESLOT_NOT_FOUND));
    }

    List<ConcertSeat> createConcertSeats(Long concertTimeslotId, Long price, List<String> seatIds) {
        findConcertTimeslot(concertTimeslotId);

        List<ConcertSeat> concertSeats = seatIds.stream().map( seatId ->
                ConcertSeat.builder()
                        .concertTimeslotId(concertTimeslotId)
                        .price(price)
                        .seatId(seatId)
                        .build()
                ).toList();
        concertSeatRepository.saveAll(concertSeats);

        ConcertTimeslotOccupancy concertTimeslotOccupancy = this.findConcertTimeslotOccupancy(concertTimeslotId);

        concertTimeslotOccupancy.increaseMaxSeatAmount(concertSeats.size());

        return concertSeats;
    }

    List<ConcertSeat> findConcertSeats(Long timeslotId) {
        return this.concertSeatRepository.findAllByConcertTimeslotIdOrderBySeatId(timeslotId);
    }

    ConcertSeat findConcertSeat(Long seatId) {
        return this.concertSeatRepository.findById(seatId)
                .orElseThrow(() -> new CommonException(CommonErrorCode.CONCERT_SEAT_NOT_FOUND));
    }

    ConcertSeatOccupyOutbox createOutbox(ConcertSeatOccupyOutbox.ConcertSeatOccupyEvent event) {
        ConcertSeatOccupyOutbox concertSeatOccupyOutbox = ConcertSeatOccupyOutbox.builder()
                .event(event)
                .createdAt(LocalDateTime.now())
                .eventStatus(ConcertSeatOccupyOutbox.EventStatus.INIT)
                .build();

        return this.concertSeatOccupyOutboxRepository.saveAndFlush(concertSeatOccupyOutbox);
    }
}
