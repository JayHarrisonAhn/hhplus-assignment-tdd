package com.example.concert.concert;

import com.example.concert.concert.domain.concert.Concert;
import com.example.concert.concert.domain.concertseat.ConcertSeat;
import com.example.concert.concert.domain.concerttimeslot.ConcertTimeslot;
import com.example.concert.concert.domain.concert.ConcertRepository;
import com.example.concert.concert.domain.concertseat.ConcertSeatRepository;
import com.example.concert.concert.domain.concerttimeslot.ConcertTimeslotRepository;
import com.example.concert.concert.domain.concerttimeslotoccupancy.ConcertTimeslotOccupancy;
import com.example.concert.concert.domain.concerttimeslotoccupancy.ConcertTimeslotOccupancyRepository;
import com.example.concert.concert.dto.ConcertTimeslotWithOccupancy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final ConcertTimeslotRepository concertTimeslotRepository;
    private final ConcertSeatRepository concertSeatRepository;
    private final ConcertTimeslotOccupancyRepository concertTimeslotOccupancyRepository;

    Concert createConcert(String name) {
        Concert concert = Concert.builder()
                .name(name)
                .build();
        concertRepository.save(concert);
        return concert;
    }

    Concert findConcert(Long concertId) {
        return concertRepository.findById(concertId)
                .orElseThrow(() -> new NoSuchElementException("No concert found with id: " + concertId));
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
                .orElseThrow(() -> new NoSuchElementException("No timeslot found with id: " + timeslotId));
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

        ConcertTimeslotOccupancy concertTimeslotOccupancy = concertTimeslotOccupancyRepository
                .findByConcertTimeslotId(concertTimeslotId);

        concertTimeslotOccupancy.increaseOccupiedSeatAmount(concertSeats.size());

        return concertSeats;
    }

    List<ConcertSeat> findConcertSeats(Long timeslotId) {
        return this.concertSeatRepository.findAllByConcertTimeslotIdOrderBySeatId(timeslotId);
    }

    ConcertSeat findConcertSeat(Long seatId) {
        return this.concertSeatRepository.findById(seatId)
                .orElseThrow(() -> new NoSuchElementException("No concert seat found with id: " + seatId));
    }
}
