package com.example.concert.application.pay.repository;

import com.example.concert.domain.PayBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PayBalanceRepository extends JpaRepository<PayBalance, Long> {

    Optional<PayBalance> findByUserId(Long userId);
}
