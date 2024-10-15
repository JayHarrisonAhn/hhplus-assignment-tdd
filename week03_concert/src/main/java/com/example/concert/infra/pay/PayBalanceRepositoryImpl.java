package com.example.concert.infra.pay;

import com.example.concert.application.pay.repository.PayBalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
public class PayBalanceRepositoryImpl implements PayBalanceRepository {
}
