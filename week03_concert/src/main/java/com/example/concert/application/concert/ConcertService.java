package com.example.concert.application.concert;

import com.example.concert.application.concert.dto.ConcertTimeslotWithOccupancy;
import com.example.concert.application.concert.repository.ConcertRepository;
import com.example.concert.application.concert.repository.ConcertSeatRepository;
import com.example.concert.application.concert.repository.ConcertTimeslotRepository;
import com.example.concert.domain.Concert;
import com.example.concert.domain.ConcertSeat;
import com.example.concert.domain.ConcertTimeslot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final ConcertTimeslotRepository concertTimeslotRepository;
    private final ConcertSeatRepository concertSeatRepository;

    Concert findConcert(Long concertId) {
        return concertRepository.findById(concertId)
                .orElseThrow(() -> new NoSuchElementException("No concert found with id: " + concertId));
    }

    List<ConcertTimeslotWithOccupancy> findConcertTimeslots(Long concertId) {
        return this.concertTimeslotRepository.findAllByConcertIdWithOccupancy(concertId);
    }

    ConcertTimeslot findConcertTimeslot(Long timeslotId) {
        return this.concertTimeslotRepository.findById(timeslotId)
                .orElseThrow(() -> new NoSuchElementException("No timeslot found with id: " + timeslotId));
    }

    List<ConcertSeat> findConcertSeats(Long timeslotId) {
        return this.concertSeatRepository.findAllByConcertTimeslotIdOrderBySeatId(timeslotId);
    }

    ConcertSeat findConcertSeat(Long seatId) {
        return this.concertSeatRepository.findById(seatId)
                .orElseThrow(() -> new NoSuchElementException("No concert seat found with id: " + seatId));
    }
}
