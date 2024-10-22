package com.example.concert.unit.domain;

import com.example.concert.balance.domain.balance.Balance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BalanceTest {

    Balance balance;

    @BeforeEach
    void setUp() {
        this.balance = Balance.builder()
                .id(1L)
                .userId(2L)
                .balance(0L)
                .build();
    }

    @Test
    @DisplayName("잔고 변경 성공 : 해피케이스")
    void deductBalance_success() {
        assertDoesNotThrow( () -> {
            balance.accumulateBalance(+5000L);
            balance.accumulateBalance(-5000L);
        });
    }

    @Test
    @DisplayName("잔고 변경 실패 : 잔고 부족")
    void deductBalance_fail_minusBalance() {
        assertDoesNotThrow( () -> {
            balance.accumulateBalance(+5000L);
        });
        assertThrows(
                IllegalStateException.class, () -> {
                    balance.accumulateBalance(-5001L);
                }
        );
    }
}