package com.example.concert.balance.domain.balancehistory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BalanceHistoryRepository extends JpaRepository<BalanceHistory, Long> {

    List<BalanceHistory> findAllByUserIdEquals(Long userId);
}
