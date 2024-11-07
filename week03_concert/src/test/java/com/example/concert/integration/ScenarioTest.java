package com.example.concert.integration;

import com.example.concert.TestEnv;
import com.example.concert.balance.BalanceFacade;
import com.example.concert.balance.domain.balancehistory.BalanceHistoryRepository;
import com.example.concert.common.error.CommonException;
import com.example.concert.concert.ConcertFacade;
import com.example.concert.token.TokenFacade;
import com.example.concert.token.domain.TokenStatus;
import com.example.concert.user.UserFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScenarioTest extends TestEnv {

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
    protected void setUp() {
        super.setUp();
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

        assertThrows(
                CommonException.class,
                () -> this.tokenFacade.check(userId, token)
        );

        this.tokenFacade.refreshTokenQueue(1);

        assertEquals(
                TokenStatus.ACTIVE,
                this.tokenFacade.check(userId, token).getStatus()
        );

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

