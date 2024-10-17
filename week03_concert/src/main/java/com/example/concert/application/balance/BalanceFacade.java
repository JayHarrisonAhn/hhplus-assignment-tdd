package com.example.concert.application.balance;

import com.example.concert.domain.BalanceHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BalanceFacade {

    private final BalanceService balanceService;

    public BalanceHistory charge(Long userId, Long amount) {
        return this.balanceService.charge(userId, amount);
    }
}
