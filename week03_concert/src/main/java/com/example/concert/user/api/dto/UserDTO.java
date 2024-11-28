package com.example.concert.user.api.dto;

import com.example.concert.token.api.dto.TokenDTO;
import com.example.concert.token.api.dto.TokenStatusDTO;
import com.example.concert.token.domain.Token;
import com.example.concert.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserDTO {

    private long id;
    private String username;

    public static UserDTO from(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
