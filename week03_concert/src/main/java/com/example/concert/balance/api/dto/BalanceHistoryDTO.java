package com.example.concert.balance.api.dto;

import com.example.concert.balance.domain.balancehistory.BalanceHistory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class BalanceHistoryDTO {

    private Long id;
    private Long userId;
    private Long amount;
    private LocalDateTime createdAt;

    public static BalanceHistoryDTO from(BalanceHistory balanceHistory) {
        return BalanceHistoryDTO.builder()
                .id(balanceHistory.getId())
                .userId(balanceHistory.getUserId())
                .amount(balanceHistory.getAmount())
                .createdAt(balanceHistory.getCreatedAt())
                .build();
    }
}