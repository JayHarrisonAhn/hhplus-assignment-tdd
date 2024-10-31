package com.example.concert.integration;

import com.example.concert.TestContainerConfig;
import com.example.concert.balance.BalanceFacade;
import com.example.concert.balance.domain.balance.BalanceRepository;
import com.example.concert.balance.domain.balancehistory.BalanceHistory;
import com.example.concert.balance.domain.balancehistory.BalanceHistoryRepository;
import com.example.concert.common.error.CommonException;
import com.example.concert.concert.ConcertFacade;
import com.example.concert.concert.dto.ConcertSeatPayInfo;
import com.example.concert.user.UserFacade;
import com.example.concert.user.domain.User;
import com.example.concert.concert.domain.concertseat.ConcertSeat;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestContainerConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ConcurrencyTest {

    @Autowired private BalanceHistoryRepository balanceHistoryRepository;
    @Autowired private BalanceRepository balanceRepository;

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

    @RepeatedTest(5)
    @DisplayName("20명이 10개 좌석에 동시에 예약 시도시, 20개의 PayHistory 발생(charge + pay)")
    void concurrent20UsersTo10Seats() {
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
        List<CompletableFuture<Optional<ConcertSeatPayInfo>>> tasks = IntStream.range(0, 20)
                .<CompletableFuture<Optional<ConcertSeatPayInfo>>>mapToObj( i -> CompletableFuture.supplyAsync(() -> {
                    try {
                        System.out.println(LocalDateTime.now());

                        User user = users.get(i);

                        Long seatId = this.concertFacade.occupyConcertSeat(
                                seats.get(i % seats.size()).getId(),
                                user.getId()
                        ).getId();

                        this.balanceFacade.charge(user.getId(), 10000L);

                        return Optional.of(this.concertFacade.paySeat(
                                seatId,
                                user.getId()
                        ));
                    } catch (CommonException ignored) {
                        return Optional.empty();
                    }
                })).toList();
        CompletableFuture
                .allOf(tasks.toArray(new CompletableFuture[0]))
                .join();

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

    @RepeatedTest(5)
    @DisplayName("200명이 1개 좌석에 동시에 예약 시도시, 1개의 좌석 예약만 발생")
    void concurrent200UsersTo1Seats() {
        // Given
        List<User> users = IntStream.range(0, 200)
                .mapToObj( i -> userFacade.createUser(String.valueOf(i)))
                .toList();

        List<ConcertSeat> seats = this.concertFacade.createConcertSeats(
                this.concertTimeslotId,
                10000L,
                IntStream.range(0, 10).mapToObj( i -> "A-" + i).toList()
        ).stream().toList();

        // When
        List<CompletableFuture<Optional<ConcertSeatPayInfo>>> tasks = IntStream.range(0, 200)
                .<CompletableFuture<Optional<ConcertSeatPayInfo>>>mapToObj( i -> CompletableFuture.supplyAsync(() -> {
                    try {
                        User user = users.get(i);

                        Long seatId = this.concertFacade.occupyConcertSeat(
                                seats.get(0).getId(),
                                user.getId()
                        ).getId();

                        this.balanceFacade.charge(user.getId(), 10000L);

                        return Optional.of(this.concertFacade.paySeat(
                                seatId,
                                user.getId()
                        ));
                    } catch (CommonException | ObjectOptimisticLockingFailureException ignored) {
                        return Optional.empty();
                    }
                })).toList();
        CompletableFuture
                .allOf(tasks.toArray(new CompletableFuture[0]))
                .join();

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

    @RepeatedTest(5)
    @DisplayName("1개의 사용자 계좌에 동시에 100건의 잔액 충전 요청")
    void concurrent100ChargeToBalance() {
        // Given
        long chargeAmount = 10000L;
        User user = userFacade.createUser("user");

        // When
        AtomicInteger success = new AtomicInteger(0);
        List<CompletableFuture<Optional<BalanceHistory>>> tasks = IntStream.range(0, 100)
                .<CompletableFuture<Optional<BalanceHistory>>>mapToObj( i -> CompletableFuture.supplyAsync(() -> {
                    try {
                        BalanceHistory balanceHistory = balanceFacade.charge(user.getId(), chargeAmount);
                        success.incrementAndGet();
                        return Optional.of(balanceHistory);
                    } catch (CommonException | ObjectOptimisticLockingFailureException ignored) {
                        return Optional.empty();
                    }
                })).toList();
        CompletableFuture
                .allOf(tasks.toArray(new CompletableFuture[0]))
                .join();

        // Then
        // Concert Timeslot 내 모든 좌석 중 한 곳에서만 예약이 되었는가
        assertEquals(
                success.get() * chargeAmount,
                this.balanceFacade.findBalanceByUserId(user.getId()).getBalance()
        );
    }
}

