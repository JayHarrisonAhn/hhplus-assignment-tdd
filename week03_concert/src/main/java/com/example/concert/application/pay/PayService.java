package com.example.concert.application.pay;

import com.example.concert.application.pay.repository.PayBalanceRepository;
import com.example.concert.application.pay.repository.PayHistoryRepository;
import com.example.concert.domain.PayBalance;
import com.example.concert.domain.PayHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class PayService {

    private final PayBalanceRepository payBalanceRepository;
    private final PayHistoryRepository payHistoryRepository;

    public PayBalance viewBalance(Long userId) {
        return payBalanceRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("No balance found for user " + userId));
    }

    public PayHistory pay(Long userId, Long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Pay amount must be greater than zero");
        }

        PayBalance balance = this.viewBalance(userId);

        balance.accumulateBalance(amount);

        PayHistory payHistory = PayHistory.builder()
                .userId(userId)
                .amount(amount)
                .createdAt(LocalDateTime.now())
                .build();

        return this.payHistoryRepository.save(payHistory);
    }
}
