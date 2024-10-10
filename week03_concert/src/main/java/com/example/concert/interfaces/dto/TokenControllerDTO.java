package com.example.concert.interfaces.dto;

import com.example.concert.interfaces.dto.entity.TokenDTO;
import lombok.Builder;
import lombok.Getter;

public class TokenControllerDTO {

    public static class Issue {
        @Getter public static class Request {
            Long userId;
        }
        @Builder @Getter public static class Response {
            TokenDTO token;
        }
    }

    public static class Check {
        @Getter public static class Request {
            Long userId;
            String token;
        }
        @Builder @Getter public static class Response {
            TokenDTO token;
        }
    }
}
