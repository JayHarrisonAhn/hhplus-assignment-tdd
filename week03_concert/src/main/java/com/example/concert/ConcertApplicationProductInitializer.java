package com.example.concert;

import com.example.concert.concert.ConcertFacade;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class ConcertApplicationProductInitializer {

    private final ConcertFacade concertFacade;

    @PostConstruct
    public void dummyData() {
        Long concertId = this.concertFacade.createConcert("아이유 연말콘서트").getId();
        Long concertTimeslotId = this.concertFacade.createConcertTimeslot(
                concertId,
                LocalDateTime.now().plusMonths(1),
                LocalDateTime.now().minusMonths(1)
        ).getId();
        Long concertSeatId = this.concertFacade.createConcertSeats(
                concertTimeslotId,
                10000L,
                List.of("A1")
        ).get(0).getId();
    }
}
