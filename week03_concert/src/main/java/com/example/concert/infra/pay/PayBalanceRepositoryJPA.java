package com.example.concert.infra.pay;

import com.example.concert.domain.PayBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayBalanceRepositoryJPA extends JpaRepository<PayBalance, Long> {
}
