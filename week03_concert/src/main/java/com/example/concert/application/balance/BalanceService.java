package com.example.concert.application.balance;

import com.example.concert.application.balance.repository.BalanceRepository;
import com.example.concert.application.balance.repository.BalanceHistoryRepository;
import com.example.concert.domain.Balance;
import com.example.concert.domain.BalanceHistory;
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
                .orElseThrow(() -> new NoSuchElementException("No balance found for user " + userId));
    }

    public BalanceHistory pay(Long userId, Long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Pay amount must be greater than zero");
        }

        return this.changeBalance(userId, -amount);
    }

    public BalanceHistory charge(Long userId, Long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Charge amount must be greater than zero");
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
