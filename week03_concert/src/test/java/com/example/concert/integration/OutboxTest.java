package com.example.concert.integration;

import com.example.concert.TestEnv;
import com.example.concert.concert.ConcertFacade;
import com.example.concert.concert.ConcertService;
import com.example.concert.concert.domain.concert.Concert;
import com.example.concert.concert.domain.concertseat.ConcertSeat;
import com.example.concert.concert.domain.concerttimeslot.ConcertTimeslot;
import com.example.concert.concert.infra.ConcertOutboxBatch;
import com.example.concert.token.TokenFacade;
import com.example.concert.token.TokenService;
import com.example.concert.user.UserFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class OutboxTest extends TestEnv {

    @Autowired private TokenFacade tokenFacade;
    @SpyBean private TokenService tokenService;

    @Autowired private ConcertFacade concertFacade;
    @SpyBean private ConcertService concertService;
    @Autowired private ConcertOutboxBatch concertOutboxBatch;

    @Autowired private UserFacade userFacade;

    @Autowired private TransactionTemplate transactionTemplate;

    Concert concert;
    ConcertTimeslot concertTimeslot;
    ConcertSeat concertSeat;
    Long userId;
    UUID token;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();

        // Given
        concert = concertFacade.createConcert("콘서트");
        concertTimeslot = concertFacade.createConcertTimeslot(
                concert.getId(),
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().plusDays(10)
        );
        concertSeat = concertFacade.createConcertSeats(
                concertTimeslot.getId(),
                10000L,
                List.of("1A")
        ).get(0);

        userId = userFacade.createUser("user").getId();
        token = tokenFacade.issue(userId).getToken();
    }

    @Test
    @DisplayName("Kafka Produce 성공 - 트랜잭션 직후 Kafka에 전달됨")
    void eventShouldConsumed_whenKafkaEventProduce() throws InterruptedException {
        // When
        concertFacade.occupyConcertSeat(
                concertSeat.getId(),
                userId,
                token.toString()
        );

        // Then
        Thread.sleep(1000);

        verify(tokenService, times(1))
                .expireToken(token.toString());

        transactionTemplate.execute( status -> {
            assertEquals(
                    0,
                    concertService.findUnproducedOutboxesBefore(LocalDateTime.now()).size()
            );
            return null;
        });
    }

    @Test
    @DisplayName("Kafka Produce 실패 - 1. 트랜잭션 직후 Kafka에 전달 안됨, Outbox에는 저장")
    void eventShouldNotProduced_whenKafkaEventProduceFails() throws InterruptedException {
        // When
        doThrow(new RuntimeException("Unknown Error"))
                .when(concertService)
                .produceKafkaMessage(any());

        concertFacade.occupyConcertSeat(
                concertSeat.getId(),
                userId,
                token.toString()
        );

        // Then
        Thread.sleep(1000);

        transactionTemplate.execute( status -> {
            assertEquals(
                    1,
                    concertService.findUnproducedOutboxesBefore(LocalDateTime.now()).size()
            );
            return null;
        });

        verify(tokenService, times(0))
                .expireToken(token.toString());
    }

    @Test
    @DisplayName("Kafka Produce 실패 - 2. Batch에 의해 Kafka에 전달됨")
    void eventShouldProducedByConsumer_whenKafkaEventProduceFails() throws InterruptedException {
        // When
        doThrow(new RuntimeException("Unknown Error"))
                .when(concertService)
                .produceKafkaMessage(any());

        concertFacade.occupyConcertSeat(
                concertSeat.getId(),
                userId,
                token.toString()
        );

        Thread.sleep(1000);

        reset(concertService);
        concertFacade.produceOldAndUnproducedOutboxes(LocalDateTime.now());

        // Then
        Thread.sleep(1000);

        verify(tokenService, times(1))
                .expireToken(token.toString());
    }
}
