package com.example.concert.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Balance {

    @Id
    @GeneratedValue
    private Long id;

    private Long userId;

    private Long balance;

    public void accumulateBalance(Long amount) {
        if (balance + amount < 0) {
            throw new IllegalStateException("Amount less than balance");
        }

        this.balance += amount;
    }
}
