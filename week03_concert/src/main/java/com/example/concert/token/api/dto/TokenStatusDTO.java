package com.example.concert.token.api.dto;

import com.example.concert.token.domain.TokenStatus;

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
