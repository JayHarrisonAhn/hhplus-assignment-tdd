package com.example.concert.interfaces.dto.entity;

import com.example.concert.domain.token.Token;
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
