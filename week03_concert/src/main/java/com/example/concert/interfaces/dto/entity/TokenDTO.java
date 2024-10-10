package com.example.concert.interfaces.dto.entity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenDTO {
    private String id;
    private TokenStatusDTO status;
}
