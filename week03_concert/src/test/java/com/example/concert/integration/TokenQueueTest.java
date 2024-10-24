package com.example.concert.integration;

import com.example.concert.balance.BalanceFacade;
import com.example.concert.balance.domain.balancehistory.BalanceHistoryRepository;
import com.example.concert.concert.ConcertFacade;
import com.example.concert.concert.domain.concert.ConcertRepository;
import com.example.concert.concert.domain.concertseat.ConcertSeatRepository;
import com.example.concert.concert.domain.concerttimeslotoccupancy.ConcertTimeslotOccupancyRepository;
import com.example.concert.concert.domain.concerttimeslot.ConcertTimeslotRepository;
import com.example.concert.token.TokenFacade;
import com.example.concert.user.UserFacade;
import com.example.concert.user.domain.UserRepository;
import com.example.concert.concert.domain.concert.Concert;
import com.example.concert.concert.domain.concertseat.ConcertSeat;
import com.example.concert.concert.domain.concerttimeslot.ConcertTimeslot;
import com.example.concert.concert.domain.concerttimeslotoccupancy.ConcertTimeslotOccupancy;
import com.example.concert.token.domain.TokenStatus;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TokenQueueTest {

    @Container
    MariaDBContainer mariaDB = new MariaDBContainer(DockerImageName.parse("mariadb:10.11"))
            .withDatabaseName("concert")
            .withUsername("username")
            .withPassword("password");

    @Autowired private ConcertRepository concertRepository;
    @Autowired private ConcertTimeslotRepository concertTimeslotRepository;
    @Autowired private ConcertTimeslotOccupancyRepository concertTimeslotOccupancyRepository;
    @Autowired private ConcertSeatRepository concertSeatRepository;
    @Autowired private BalanceHistoryRepository balanceHistoryRepository;
    @Autowired private UserRepository userRepository;

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
        // 시간 부족으로 인해 concert data save용 service와 facade를 작성하지 못했습니다..ㅠㅠ
        // repository를 레어하게 이용해서 데이터를 저장하는 것은 분명 부족한 부분입니다
        // 다음주차에 해당 기능들을 application에서 구현하도록 하겠습니다.
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
                            .concertTimeslotId(this.concertTimeslotId)
                            .id((long) i)
                            .seatId("A-" + i)
                            .price(10000L)
                            .build()
            );
        }
    }

    @AfterEach
    void tearDown() {
        this.userRepository.deleteAll();
    }

    @Test
    @DisplayName("20명이 15개 좌석에 동시에 예약 시도시, 30개의 PayHistory 발생(charge + pay)")
    void concurrent20UsersTo15Seats() {
        // When
        ExecutorService executor = Executors.newFixedThreadPool(20);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
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
        // 예약 시도한 User의 PayHistory 갯수가 maxNumOfSeats의 2배인가(charge+pay)
        long numOfPayHistories = 0;
        for (int i = 1; i <= 20; i++) {
            Long userId = this.userFacade.findByUsername("user"+i).getId();
            numOfPayHistories += this.balanceHistoryRepository.findAllByUserIdEquals(userId).size();
        }
        assertEquals(this.maxNumOfSeats * 2, numOfPayHistories);
    }

    @Test
    @DisplayName("20명이 1개 좌석에 동시에 예약 시도시, 1개의 좌석 예약만 발생")
    void concurrent20UsersTo1Seats() {
        // When
        ExecutorService executor = Executors.newFixedThreadPool(20);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
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
                            1L,
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
        // Concert Timeslot 내 모든 좌석 중 한 곳에서만 예약이 되었는가
        assertEquals(1,
                this.concertSeatRepository
                        .findAllByConcertTimeslotIdOrderBySeatId(this.concertTimeslotId)
                        .stream()
                        .filter( seat -> seat.getUserId() != null)
                        .count()
        );

        // 예약 시도한 User의 PayHistory 갯수가 2개인가(charge+pay)
        long numOfPayHistories = 0;
        for (int i = 1; i <= 20; i++) {
            Long userId = this.userFacade.findByUsername("user"+i).getId();
            numOfPayHistories += this.balanceHistoryRepository.findAllByUserIdEquals(userId).size();
        }
        assertEquals(2, numOfPayHistories);
    }
}
