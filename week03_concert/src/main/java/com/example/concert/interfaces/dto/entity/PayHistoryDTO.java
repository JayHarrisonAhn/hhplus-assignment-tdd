package com.example.concert.interfaces.dto.entity;

import com.example.concert.domain.PayHistory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class PayHistoryDTO {

    private Long id;
    private Long userId;
    private Long amount;
    private LocalDateTime createdAt;

    public static PayHistoryDTO from(PayHistory payHistory) {
        return PayHistoryDTO.builder()
                .id(payHistory.getId())
                .userId(payHistory.getUserId())
                .amount(payHistory.getAmount())
                .createdAt(payHistory.getCreatedAt())
                .build();
    }
}
