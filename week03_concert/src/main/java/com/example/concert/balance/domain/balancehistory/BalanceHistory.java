package com.example.concert.balance.domain.balancehistory;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceHistory {

    @Id
    @GeneratedValue
    private Long id;

    private Long userId;

    private Long amount;

    private LocalDateTime createdAt;
}
