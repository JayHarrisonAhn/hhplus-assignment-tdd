package com.example.concert.interfaces.dto.entity;

import com.example.concert.domain.enums.TokenStatus;

public enum TokenStatusDTO {
    WAIT,
    ACTIVE,
    EXPIRED;

    public static TokenStatusDTO from(TokenStatus status) {
        return switch (status) {
            case WAIT -> TokenStatusDTO.WAIT;
            case ACTIVE -> TokenStatusDTO.ACTIVE;
            case EXPIRED -> TokenStatusDTO.EXPIRED;
        };
    }
}
