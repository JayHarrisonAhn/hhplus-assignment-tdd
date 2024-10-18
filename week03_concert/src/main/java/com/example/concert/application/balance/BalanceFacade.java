package com.example.concert.application.balance;

import com.example.concert.domain.BalanceHistory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Transactional
public class BalanceFacade {

    private final BalanceService balanceService;

    public BalanceHistory charge(Long userId, Long amount) {
        return this.balanceService.charge(userId, amount);
    }
}
