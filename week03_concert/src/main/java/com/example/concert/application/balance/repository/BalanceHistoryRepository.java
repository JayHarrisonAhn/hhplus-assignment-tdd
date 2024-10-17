package com.example.concert.application.balance.repository;

import com.example.concert.domain.BalanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceHistoryRepository extends JpaRepository<BalanceHistory, Long> {
}
