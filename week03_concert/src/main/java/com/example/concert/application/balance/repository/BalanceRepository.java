package com.example.concert.application.balance.repository;

import com.example.concert.domain.Balance;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface BalanceRepository extends JpaRepository<Balance, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Balance> findByUserId(Long userId);
}
