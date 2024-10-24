package com.example.concert.balance.domain.balance;

import com.example.concert.common.error.CommonErrorCode;
import com.example.concert.common.error.CommonException;
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
            throw new CommonException(CommonErrorCode.BALANCE_INSUFFICIENT);
        }

        this.balance += amount;
    }
}
