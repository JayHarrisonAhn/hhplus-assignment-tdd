package com.example.concert.balance;

import com.example.concert.balance.domain.balance.Balance;
import com.example.concert.balance.domain.balancehistory.BalanceHistory;
import com.example.concert.balance.domain.balancehistory.BalanceHistoryRepository;
import com.example.concert.balance.domain.balance.BalanceRepository;
import com.example.concert.common.error.CommonErrorCode;
import com.example.concert.common.error.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class BalanceService {

    private final BalanceRepository balanceRepository;
    private final BalanceHistoryRepository balanceHistoryRepository;

    public Balance createBalance(Long userId) {
        return balanceRepository.save(
                Balance.builder()
                        .userId(userId)
                        .balance(0L)
                        .build()
        );
    }

    public Balance viewBalance(Long userId) {
        return balanceRepository.findByUserId(userId)
                .orElseThrow(() -> new CommonException(CommonErrorCode.USER_NOT_FOUND));
    }

    public BalanceHistory pay(Long userId, Long amount) {
        if (amount <= 0) {
            throw new CommonException(CommonErrorCode.BALANCE_TRANSACTION_AMOUNT_LESS_THAN_ZERO);
        }

        return this.changeBalance(userId, -amount);
    }

    public BalanceHistory charge(Long userId, Long amount) {
        if (amount <= 0) {
            throw new CommonException(CommonErrorCode.BALANCE_TRANSACTION_AMOUNT_LESS_THAN_ZERO);
        }

        return this.changeBalance(userId, amount);
    }

    public BalanceHistory changeBalance(Long userId, Long amount) {
        Balance balance = this.viewBalance(userId);

        balance.accumulateBalance(amount);

        BalanceHistory balanceHistory = BalanceHistory.builder()
                .userId(userId)
                .amount(amount)
                .createdAt(LocalDateTime.now())
                .build();

        return this.balanceHistoryRepository.save(balanceHistory);
    }
}
