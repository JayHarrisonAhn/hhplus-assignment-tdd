package com.example.concert.unit.service;

import com.example.concert.balance.domain.balance.BalanceRepository;
import com.example.concert.balance.domain.balancehistory.BalanceHistoryRepository;
import com.example.concert.balance.BalanceService;
import com.example.concert.balance.domain.balance.Balance;
import com.example.concert.common.error.CommonException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {

    @InjectMocks
    private BalanceService balanceService;

    @Mock
    private BalanceRepository balanceRepository;

    @Mock
    private BalanceHistoryRepository balanceHistoryRepository;

    @Test
    @DisplayName("결제 성공 : 해피케이스")
    void pay_success() {
        // Given
        Long userId = 1L;
        Long balanceAmount = 5000L;
        Long amount = 1000L;

        Balance balance = Mockito.spy(
                Balance.builder()
                        .id(2L)
                        .userId(userId)
                        .balance(balanceAmount)
                        .build()
        );

        when(
                balanceRepository.findByUserId(userId)
        ).thenReturn(
                Optional.ofNullable(balance)
        );

        // When
        balanceService.pay(userId, amount);

        // Then
        Mockito.verify(balance).accumulateBalance(-amount);
    }

    @Test
    @DisplayName("결제 실패 : 마이너스 결제 금액")
    void pay_fail() {
        // Given
        Long userId = 1L;
        Long amount = -1000L;

        // When, Then
        assertThrows(
                CommonException.class, () -> {
                    balanceService.pay(userId, amount);
                }
        );
    }

    @Test
    @DisplayName("충전 성공 : 해피케이스")
    void charge_success() {
        // Given
        Long userId = 1L;
        Long balanceAmount = 5000L;
        Long amount = 1000L;

        Balance balance = Mockito.spy(
                Balance.builder()
                        .id(2L)
                        .userId(userId)
                        .balance(balanceAmount)
                        .build()
        );

        when(
                balanceRepository.findByUserId(userId)
        ).thenReturn(
                Optional.ofNullable(balance)
        );

        // When
        balanceService.charge(userId, amount);

        // Then
        Mockito.verify(balance).accumulateBalance(amount);
    }

    @Test
    @DisplayName("충전 실패 : 마이너스 충전 금액")
    void charge_fail() {
        // Given
        Long userId = 1L;
        Long amount = -1000L;

        // When, Then
        assertThrows(
                CommonException.class, () -> {
                    balanceService.pay(userId, amount);
                }
        );
    }
}