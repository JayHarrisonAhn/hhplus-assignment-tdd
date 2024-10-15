package com.example.concert.infra.pay;

import com.example.concert.domain.PayHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayHistoryRepositoryJPA extends JpaRepository<PayHistory, Long> {
}
