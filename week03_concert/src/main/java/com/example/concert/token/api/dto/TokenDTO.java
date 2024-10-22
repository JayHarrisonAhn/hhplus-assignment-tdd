package com.example.concert.token.api.dto;

import com.example.concert.token.domain.Token;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenDTO {
    private String id;
    private TokenStatusDTO status;

    public static TokenDTO from(Token token) {
        return TokenDTO.builder()
                .id(token.getToken().toString())
                .status(TokenStatusDTO.from(token.getStatus()))
                .build();
    }
}
