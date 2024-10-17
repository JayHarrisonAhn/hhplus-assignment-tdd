package com.example.concert.integration;

import com.example.concert.application.balance.BalanceFacade;
import com.example.concert.application.balance.repository.BalanceHistoryRepository;
import com.example.concert.application.concert.ConcertFacade;
import com.example.concert.application.concert.repository.ConcertRepository;
import com.example.concert.application.concert.repository.ConcertSeatRepository;
import com.example.concert.application.concert.repository.ConcertTimeslotOccupancyRepository;
import com.example.concert.application.concert.repository.ConcertTimeslotRepository;
import com.example.concert.application.token.TokenFacade;
import com.example.concert.application.user.UserFacade;
import com.example.concert.domain.*;
import com.example.concert.domain.token.Token;
import com.example.concert.domain.token.TokenStatus;
import com.example.concert.infra.Scheduler;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TokenQueueTest {

    @Autowired private Scheduler scheduler;

    @Autowired private ConcertRepository concertRepository;
    @Autowired private ConcertTimeslotRepository concertTimeslotRepository;
    @Autowired private ConcertTimeslotOccupancyRepository concertTimeslotOccupancyRepository;
    @Autowired private ConcertSeatRepository concertSeatRepository;
    @Autowired private BalanceHistoryRepository balanceHistoryRepository;

    @Autowired private TokenFacade tokenFacade;
    @Autowired private BalanceFacade balanceFacade;
    @Autowired private ConcertFacade concertFacade;
    @Autowired private UserFacade userFacade;

    Long concertId;
    Long concertTimeslotId;
    Integer maxNumOfSeats = 13;

    @Autowired private TransactionTemplate template;

    @BeforeEach
    void setUp() {
        this.concertId = concertRepository.save(
                Concert.builder()
                        .id(1L)
                        .name("아이유 연말콘서트")
                        .build()
        ).getId();
        this.concertTimeslotId = concertTimeslotRepository.save(
                ConcertTimeslot.builder()
                        .id(1L)
                        .concertId(this.concertId)
                        .concertStartTime(LocalDateTime.of(2024, 12, 25, 20, 0))
                        .reservationStartTime(LocalDateTime.of(2024, 9, 25, 20, 0))
                        .build()
        ).getId();
        concertTimeslotOccupancyRepository.save(
                ConcertTimeslotOccupancy.builder()
                        .concertTimeslotId(this.concertTimeslotId)
                        .maxSeatAmount(maxNumOfSeats)
                        .occupiedSeatAmount(0)
                        .build()
        );
        for (int i = 1; i <= maxNumOfSeats; i++) {
            concertSeatRepository.save(
                    ConcertSeat.builder()
                            .id((long) i)
                            .seatId("A-" + i)
                            .price(10000L)
                            .build()
            );
        }
    }

    @Test
    @DisplayName("20명이 15개 좌석에 동시에 예약 시도시, 30개의 PayHistory 발생(charge + pay)")
    void concurrent_20() {
        // When
        ExecutorService executor = Executors.newFixedThreadPool(20);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        AtomicReference<LocalDateTime> registeredTime = new AtomicReference<>();
        for (int i = 1; i <= 20; i++) {
            long taskId = i; // Assign a unique task number
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                System.out.println(taskId + "th user started to register on thread " + Thread.currentThread().getName());
                try {
                    Long userId = this.userFacade.createUser("user" + taskId).getId();

                    String token = this.tokenFacade.issue(userId).getToken().toString();

                    do {
                        Thread.sleep(1000);
                    } while (this.tokenFacade.check(userId, token).getStatus().equals(TokenStatus.WAIT));

                    Long seatId = this.concertFacade.occupyConcertSeat(
                            (taskId % maxNumOfSeats) + 1,
                            userId,
                            token
                    ).getId();

                    this.balanceFacade.charge(userId, 10000L);

                    this.concertFacade.paySeat(
                            seatId,
                            userId,
                            token
                    );
                    System.out.println(taskId + "th user Succeeded");
                } catch (Exception ignored) {
                    System.out.println(taskId + "th user Failed : " + ignored.getMessage());
                }
                System.out.println(taskId + "th user Finished");
            }, executor);
            futures.add(future);
        }
        CompletableFuture<Void> allOfFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        assertDoesNotThrow( () -> allOfFutures.get() );
        executor.shutdown();
        System.out.println("All tasks completed.");

        // Then
        long numOfPayHistories = 0;
        for (int i = 1; i <= 20; i++) {
            Long userId = this.userFacade.findByUsername("user"+i).getId();
            numOfPayHistories += this.balanceHistoryRepository.findAllByUserIdEquals(userId).size();
        }
        assertEquals(this.maxNumOfSeats * 2, numOfPayHistories);
    }
}
