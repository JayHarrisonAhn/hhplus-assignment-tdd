package com.example.concert.token.api;

import com.example.concert.token.api.dto.TokenDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TokenControllerDTO {

    public static class Issue {
        @Builder @Getter public static class Request { }
        @Builder @Getter public static class Response {
            TokenDTO token;
        }
    }

    public static class Check {
        @Builder @Getter public static class Request { }
        @Builder @Getter public static class Response {
            TokenDTO token;
        }
    }
}
