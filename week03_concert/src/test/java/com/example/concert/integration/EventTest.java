package com.example.concert.integration;

import com.example.concert.TestEnv;
import com.example.concert.concert.ConcertFacade;
import com.example.concert.concert.domain.concert.Concert;
import com.example.concert.concert.domain.concertseat.ConcertSeat;
import com.example.concert.concert.domain.concerttimeslot.ConcertTimeslot;
import com.example.concert.token.TokenFacade;
import com.example.concert.user.UserFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class EventTest extends TestEnv {

    @SpyBean private TokenFacade tokenFacade;
    @Autowired private ConcertFacade concertFacade;
    @Autowired private UserFacade userFacade;

    @Test
    void tokenShouldExpire_afterConcertSeatOccupy() throws InterruptedException {
        // Given
        Concert concert = concertFacade.createConcert("콘서트");
        ConcertTimeslot concertTimeslot = concertFacade.createConcertTimeslot(
                concert.getId(),
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().plusDays(10)
        );
        ConcertSeat concertSeat = concertFacade.createConcertSeats(
                concertTimeslot.getId(),
                10000L,
                List.of("1A")
        ).get(0);

        Long userId = userFacade.createUser("user").getId();
        UUID token = tokenFacade.issue(userId).getToken();

        // When
        concertFacade.occupyConcertSeat(
                concertSeat.getId(),
                userId,
                token.toString()
        );

        // Then
        Thread.sleep(1000);
        verify(tokenFacade, times(1))
                .expireToken(token.toString());
    }
}
