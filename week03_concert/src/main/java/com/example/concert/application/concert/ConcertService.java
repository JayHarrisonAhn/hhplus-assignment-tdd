package com.example.concert.application.concert;

import com.example.concert.application.concert.dto.ConcertTimeslotWithOccupancy;
import com.example.concert.application.concert.repository.ConcertRepository;
import com.example.concert.application.concert.repository.ConcertTimeslotRepository;
import com.example.concert.domain.Concert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final ConcertTimeslotRepository concertTimeslotRepository;

    Concert findConcert(Long concertId) {
        return concertRepository.findById(concertId)
                .orElseThrow(() -> new NoSuchElementException("No concert found with id: " + concertId));
    }

    List<ConcertTimeslotWithOccupancy> findConcertTimeslots(Long concertId) {
        return this.concertTimeslotRepository.findAllByConcertIdWithOccupancy(concertId);
    }
}
