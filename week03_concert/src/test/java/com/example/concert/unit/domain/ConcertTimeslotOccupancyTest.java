package com.example.concert.unit.domain;

import com.example.concert.common.error.CommonException;
import com.example.concert.concert.domain.concerttimeslotoccupancy.ConcertTimeslotOccupancy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConcertTimeslotOccupancyTest {

    ConcertTimeslotOccupancy timeslotOccupancy;

    @BeforeEach
    void setUp() {
        this.timeslotOccupancy = ConcertTimeslotOccupancy.builder()
                .concertTimeslotId(1L)
                .maxSeatAmount(100)
                .occupiedSeatAmount(50)
                .build();
    }

    @Test
    @DisplayName("최대 좌석 갯수 증가 성공 : 해피케이스")
    void increaseMaxSeatAmount_success() {
        assertDoesNotThrow( () -> {
            timeslotOccupancy.increaseMaxSeatAmount(100);
        });
    }

    @Test
    @DisplayName("점유된 좌석 갯수 증가 성공 : 해피케이스")
    void increaseOccupiedSeatAmount_success() {
        assertDoesNotThrow( () -> {
            timeslotOccupancy.increaseOccupiedSeatAmount();
        });
    }

    @Test
    @DisplayName("점유된 좌석 갯수 증가 실패 : 만석")
    void increaseOccupiedSeatAmount_fail_overMaxSeatAmount() {
        assertDoesNotThrow( () -> {
            for (int i=0; i < 50; i++) {
                timeslotOccupancy.increaseOccupiedSeatAmount();
            }
        });
        assertThrows(CommonException.class, () -> {
            timeslotOccupancy.increaseOccupiedSeatAmount();
        });
    }
}