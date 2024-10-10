package com.example.concert.interfaces.dto.entity;

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
}
