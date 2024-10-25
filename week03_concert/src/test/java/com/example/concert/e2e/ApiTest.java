package com.example.concert.e2e;

import com.example.concert.balance.api.BalanceControllerDTO;
import com.example.concert.common.error.CommonErrorCode;
import com.example.concert.concert.ConcertFacade;
import com.example.concert.concert.domain.concertseat.ConcertSeat;
import com.example.concert.user.UserFacade;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ApiTest {

    @LocalServerPort
    private int port;

    @Autowired private UserFacade userFacade;
    @Autowired private ConcertFacade concertFacade;

    Long userId;
    Long concertId;
    Long concertTimeslotId;
    List<Long> concertSeatIds;

    @BeforeEach
    void setUp() {
        this.userId = this.userFacade.createUser("user aaa").getId();
        this.concertId = this.concertFacade.createConcert("아이유 연말콘서트").getId();
        this.concertTimeslotId = this.concertFacade.createConcertTimeslot(
                this.concertId,
                LocalDateTime.now().plusMonths(1),
                LocalDateTime.now().minusMonths(1)
        ).getId();
        this.concertSeatIds = this.concertFacade.createConcertSeats(
                this.concertTimeslotId,
                10000L,
                IntStream.range(0, 10).mapToObj(i -> "A-" + i).toList()
        ).stream().map(ConcertSeat::getId).toList();
    }

    @Test
    @DisplayName("잔액 충전")
    void shouldFail_concertAccessWithoutToken() {
        // When
        Integer historyBalance = RestAssured.given()
                .port(port)
                .header("Authorization", userId)
                .header("Content-Type", "application/json")
                .body(
                        new BalanceControllerDTO.Charge.Request(10000L)
                )
                .when().post("/pay/charge")
                .then()
                .extract().path("balanceHistory.amount");

        // Then
        assertEquals(10000, historyBalance);
    }

    @Test
    @DisplayName("콘서트 시간 확인")
    void shouldFail_concertTimeslot() {
        // Given
        String token = RestAssured.given()
                .port(port)
                .header("Authorization", userId)
                .log().all()
                .when().post("token")
                .then().log().all()
                .extract().path("token.id");

        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // When, Then
        RestAssured.given()
                .port(port)
                .header("Authorization", userId)
                .header("Token", token)
                .header("Content-Type", "application/json")
                .body(
                        new BalanceControllerDTO.Charge.Request(10000L)
                )
                .when().get("/concert/"+concertId+"/timeSlot/")
                .then().log().all()
                .extract();
    }

    @Test
    @DisplayName("콘서트 좌석 확인")
    void shouldFail_concertSeat() {
        // Given
        String token = RestAssured.given()
                .port(port)
                .header("Authorization", userId)
                .log().all()
                .when().post("token")
                .then().log().all()
                .extract().path("token.id");

        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // When, Then
        RestAssured.given()
                .port(port)
                .header("Authorization", userId)
                .header("Token", token)
                .header("Content-Type", "application/json")
                .body(
                        new BalanceControllerDTO.Charge.Request(10000L)
                )
                .when().get("/concert/"+concertId+"/timeSlot/"+concertTimeslotId+"/seat")
                .then()
                .extract();
    }
}
