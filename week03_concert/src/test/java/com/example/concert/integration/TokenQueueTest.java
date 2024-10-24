package com.example.concert.integration;

import com.example.concert.balance.BalanceFacade;
import com.example.concert.balance.domain.balancehistory.BalanceHistoryRepository;
import com.example.concert.concert.ConcertFacade;
import com.example.concert.token.TokenFacade;
import com.example.concert.user.UserFacade;
import com.example.concert.user.domain.User;
import com.example.concert.concert.domain.concertseat.ConcertSeat;
import com.example.concert.token.domain.TokenStatus;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TokenQueueTest {

    @Autowired private BalanceHistoryRepository balanceHistoryRepository;

    @Autowired private TokenFacade tokenFacade;
    @Autowired private BalanceFacade balanceFacade;
    @Autowired private ConcertFacade concertFacade;
    @Autowired private UserFacade userFacade;

    Long concertId;
    Long concertTimeslotId;

    @BeforeEach
    void setUp() {
        this.concertId = this.concertFacade.createConcert("아이유 연말콘서트").getId();
        this.concertTimeslotId = this.concertFacade.createConcertTimeslot(
                this.concertId,
                LocalDateTime.now().plusMonths(1),
                LocalDateTime.now().minusMonths(1)
        ).getId();
    }

    @Test
    @DisplayName("20명이 15개 좌석에 동시에 예약 시도시, 30개의 PayHistory 발생(charge + pay)")
    void concurrent20UsersTo15Seats() {
        // Given
        List<User> users = IntStream.range(0, 20)
                .mapToObj( i -> userFacade.createUser(String.valueOf(i)))
                .toList();

        List<ConcertSeat> seats = this.concertFacade.createConcertSeats(
                this.concertTimeslotId,
                10000L,
                IntStream.range(0, 10).mapToObj( i -> "A-" + i).toList()
        ).stream().toList();

        // When
        ExecutorService executor = Executors.newFixedThreadPool(users.size());
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        IntStream.range(0, users.size()).forEach( i -> {
            User user = users.get(i);
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                System.out.println(user.getId() + " user started to register on thread " + Thread.currentThread().getName());
                try {
                    String token = this.tokenFacade.issue(user.getId()).getToken().toString();

                    do {
                        Thread.sleep(1000);
                    } while (this.tokenFacade.check(user.getId(), token).getStatus().equals(TokenStatus.WAIT));

                    Long seatId = this.concertFacade.occupyConcertSeat(
                            seats.get(i % seats.size()).getId(),
                            user.getId(),
                            token
                    ).getId();

                    this.balanceFacade.charge(user.getId(), 10000L);

                    this.concertFacade.paySeat(
                            seatId,
                            user.getId(),
                            token
                    );
                    System.out.println(user.getId() + " user Succeeded");
                } catch (Exception ignored) {
                    System.out.println(user.getId() + " user Failed : " + ignored.getMessage());
                }
                System.out.println(user.getId() + " user Finished");
            }, executor);
            futures.add(future);
        });

        CompletableFuture<Void> allOfFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        assertDoesNotThrow( () -> allOfFutures.get() );
        executor.shutdown();
        System.out.println("All tasks completed.");

        // Then
        // 예약 시도한 User의 PayHistory 갯수가 maxNumOfSeats의 2배인가(charge+pay)
        assertEquals(
                seats.size() * 2,
                users.stream().mapToInt( user ->
                    this.balanceHistoryRepository.findAllByUserIdEquals(
                            user.getId()
                    ).size()
                ).reduce(0, Integer::sum)
        );
    }

    @Test
    @DisplayName("20명이 1개 좌석에 동시에 예약 시도시, 1개의 좌석 예약만 발생")
    void concurrent20UsersTo1Seats() {
        // Given
        List<User> users = IntStream.range(0, 20)
                .mapToObj( i -> userFacade.createUser(String.valueOf(i)))
                .toList();

        List<ConcertSeat> seats = this.concertFacade.createConcertSeats(
                this.concertTimeslotId,
                10000L,
                IntStream.range(0, 10).mapToObj( i -> "A-" + i).toList()
        ).stream().toList();

        // When
        ExecutorService executor = Executors.newFixedThreadPool(20);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        IntStream.range(0, users.size()).forEach( i -> {
            User user = users.get(i);
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                System.out.println(i + "th user started to register on thread " + Thread.currentThread().getName());
                try {
                    String token = this.tokenFacade.issue(user.getId()).getToken().toString();

                    do {
                        Thread.sleep(1000);
                    } while (this.tokenFacade.check(user.getId(), token).getStatus().equals(TokenStatus.WAIT));

                    Long seatId = this.concertFacade.occupyConcertSeat(
                            seats.get(0).getId(),
                            user.getId(),
                            token
                    ).getId();

                    this.balanceFacade.charge(user.getId(), 10000L);

                    this.concertFacade.paySeat(
                            seatId,
                            user.getId(),
                            token
                    );
                    System.out.println(i + "th user Succeeded");
                } catch (Exception ignored) {
                    System.out.println(i + "th user Failed : " + ignored.getMessage());
                }
                System.out.println(i + "th user Finished");
            }, executor);
            futures.add(future);
        });
        CompletableFuture<Void> allOfFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        assertDoesNotThrow( () -> allOfFutures.get() );
        executor.shutdown();
        System.out.println("All tasks completed.");

        // Then
        // Concert Timeslot 내 모든 좌석 중 한 곳에서만 예약이 되었는가
        assertEquals(1,
                this.concertFacade
                        .findConcertSeats(this.concertTimeslotId)
                        .stream()
                        .filter( seat -> seat.getUserId() != null)
                        .count()
        );

        // 예약 시도한 User의 PayHistory 갯수가 2개인가(charge+pay)
        assertEquals(
                2,
                users.stream().mapToInt( user ->
                        this.balanceHistoryRepository.findAllByUserIdEquals(
                                user.getId()
                        ).size()
                ).reduce(0, Integer::sum)
        );
    }
}
