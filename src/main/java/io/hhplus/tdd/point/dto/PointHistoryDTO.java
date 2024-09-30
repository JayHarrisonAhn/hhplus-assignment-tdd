package io.hhplus.tdd.point.dto;

import io.hhplus.tdd.point.entity.TransactionType;

public record PointHistoryDTO(
        long userId,
        long amount,
        TransactionType type,
        long updateMillis
) {
}
