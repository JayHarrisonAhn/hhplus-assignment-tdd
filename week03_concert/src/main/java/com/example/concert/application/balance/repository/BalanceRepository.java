package com.example.concert.application.balance.repository;

import com.example.concert.domain.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BalanceRepository extends JpaRepository<Balance, Long> {

    Optional<Balance> findByUserId(Long userId);
}
