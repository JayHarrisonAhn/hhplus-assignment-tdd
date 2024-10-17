package com.example.concert.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PayBalanceTest {

    PayBalance payBalance;

    @BeforeEach
    void setUp() {
        this.payBalance = PayBalance.builder()
                .id(1L)
                .userId(2L)
                .balance(0L)
                .build();
    }

    @Test
    @DisplayName("잔고 변경 성공 : 해피케이스")
    void deductBalance_success() {
        assertDoesNotThrow( () -> {
            payBalance.accumulateBalance(+5000L);
            payBalance.accumulateBalance(-5000L);
        });
    }

    @Test
    @DisplayName("잔고 변경 실패 : 잔고 부족")
    void deductBalance_fail_minusBalance() {
        assertDoesNotThrow( () -> {
            payBalance.accumulateBalance(+5000L);
        });
        assertThrows(
                IllegalStateException.class, () -> {
                    payBalance.accumulateBalance(-5001L);
                }
        );
    }
}