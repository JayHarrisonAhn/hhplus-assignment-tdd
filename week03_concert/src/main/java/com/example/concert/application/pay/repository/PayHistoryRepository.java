package com.example.concert.application.pay.repository;

import com.example.concert.domain.PayHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayHistoryRepository extends JpaRepository<PayHistory, Long> {
}
