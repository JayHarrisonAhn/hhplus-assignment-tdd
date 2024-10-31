package com.example.concert.balance;

import com.example.concert.balance.domain.balance.Balance;
import com.example.concert.balance.domain.balancehistory.BalanceHistory;
import com.example.concert.common.redissonlock.RedissonLock;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BalanceFacade {

    private final BalanceService balanceService;

    @Transactional
    @RedissonLock("balance:{userId}")
    public BalanceHistory charge(Long userId, Long amount) {
        return this.balanceService.charge(userId, amount);
    }

    @Transactional
    @RedissonLock("balance:{userId}")
    public Balance findBalanceByUserId(Long userId) {
        return this.balanceService.viewBalance(userId);
    }
}
