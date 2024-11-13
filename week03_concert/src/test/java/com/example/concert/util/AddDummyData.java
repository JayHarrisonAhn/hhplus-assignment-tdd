package com.example.concert.util;

import com.example.concert.concert.ConcertFacade;
import com.example.concert.concert.domain.concert.Concert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

@SpringBootTest
@ActiveProfiles("dev")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AddDummyData {

    @Autowired private ConcertFacade concertFacade;

    @Test
    @Disabled
    void insertDummyData() {
        for (int i = 0; i < 100; i++) {
            // Create Concert
            Concert concert = concertFacade.createConcert("콘서트 " + i);

            for (int j=0; j<100; j++) {
                LocalDateTime concertDateTime = LocalDateTime.now().plusDays(10 + j);
                concertFacade.createConcertTimeslot(
                        concert.getId(),
                        concertDateTime,
                        concertDateTime.minusDays(1)
                );
            }
        }
    }
}
