package com.example.concert.application.balance.repository;

import com.example.concert.domain.BalanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BalanceHistoryRepository extends JpaRepository<BalanceHistory, Long> {

    List<BalanceHistory> findAllByUserIdEquals(Long userId);
}
