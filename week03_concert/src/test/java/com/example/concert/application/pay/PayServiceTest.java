package com.example.concert.application.pay;

import com.example.concert.application.pay.repository.PayBalanceRepository;
import com.example.concert.application.pay.repository.PayHistoryRepository;
import com.example.concert.domain.PayBalance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class PayServiceTest {

    @InjectMocks
    private PayService payService;

    @Mock
    private PayBalanceRepository payBalanceRepository;

    @Mock
    private PayHistoryRepository payHistoryRepository;

    @Test
    @DisplayName("결제 성공 : 해피케이스")
    void pay_success() {
        // Given
        Long userId = 1L;
        Long balance = 5000L;
        Long amount = 1000L;

        when(
                payBalanceRepository.findByUserId(userId)
        ).thenReturn(Optional.ofNullable(
                PayBalance.builder()
                        .id(2L)
                        .userId(userId)
                        .balance(balance)
                        .build())
        );

        // When, Then
        assertDoesNotThrow(() -> {
            payService.pay(userId, amount);
        });
    }

    @Test
    @DisplayName("결제 실패 : 마이너스 결제 금액")
    void pay_fail() {
        // Given
        Long userId = 1L;
        Long amount = -1000L;

        // When, Then
        assertThrows(
                IllegalArgumentException.class, () -> {
                    payService.pay(userId, amount);
                }
        );
    }
}