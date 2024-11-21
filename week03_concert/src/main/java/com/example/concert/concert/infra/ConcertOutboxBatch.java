package com.example.concert.concert.infra;

import com.example.concert.concert.ConcertFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ConcertOutboxBatch {

    private final ConcertFacade concertFacade;

    @Scheduled(fixedRate = 30000)
    public void batch() {
        concertFacade.produceOldAndUnproducedOutboxes(LocalDateTime.now().minusMinutes(3));
    }
}
