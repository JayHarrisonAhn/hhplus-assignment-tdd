package com.example.concert.integration;

import com.example.concert.balance.BalanceFacade;
import com.example.concert.balance.domain.balancehistory.BalanceHistoryRepository;
import com.example.concert.common.error.CommonException;
import com.example.concert.concert.ConcertFacade;
import com.example.concert.concert.domain.concertseat.ConcertSeat;
import com.example.concert.token.TokenFacade;
import com.example.concert.token.domain.TokenStatus;
import com.example.concert.user.UserFacade;
import com.example.concert.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ScenarioTest {

    @Autowired private BalanceHistoryRepository balanceHistoryRepository;

    @Autowired private TokenFacade tokenFacade;
    @Autowired private BalanceFacade balanceFacade;
    @Autowired private ConcertFacade concertFacade;
    @Autowired private UserFacade userFacade;

    Long userId;
    Long concertId;
    Long concertTimeslotId;
    Long concertSeatId;

    @BeforeEach
    void setUp() {
        this.userId = this.userFacade.createUser("user name").getId();
        this.concertId = this.concertFacade.createConcert("아이유 연말콘서트").getId();
        this.concertTimeslotId = this.concertFacade.createConcertTimeslot(
                this.concertId,
                LocalDateTime.now().plusMonths(1),
                LocalDateTime.now().minusMonths(1)
        ).getId();
        this.concertSeatId = this.concertFacade.createConcertSeats(
                concertTimeslotId,
                10000L,
                List.of("A1")
        ).get(0).getId();
    }

    @Test
    @DisplayName("토큰 발급부터 좌석 예매까지 해피케이스")
    void success_reservation() throws Exception {
        // When
        String token = this.tokenFacade.issue(userId).getToken().toString();
        do {
            Thread.sleep(1000);
        } while (this.tokenFacade.check(userId, token).getStatus().equals(TokenStatus.WAIT));

        Long seatId = this.concertFacade.occupyConcertSeat(
                concertSeatId,
                userId
        ).getId();

        this.balanceFacade.charge(userId, 10000L);

        this.concertFacade.paySeat(
                seatId,
                userId
        );

        // Then
        // 예약 시도한 User의 PayHistory 갯수가 2개인가(charge+pay)
        assertEquals(
                2,
                this.balanceHistoryRepository.findAllByUserIdEquals(
                        userId
                ).size()
        );
    }
}
