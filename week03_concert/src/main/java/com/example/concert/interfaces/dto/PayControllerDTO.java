package com.example.concert.interfaces.dto;

import lombok.Builder;
import lombok.Getter;

public class PayControllerDTO {

    public static class Charge {
        @Getter public static class Request {
            Long userId;
        }
        @Builder @Getter public static class Response {
            Long balance;
        }
    }
}
