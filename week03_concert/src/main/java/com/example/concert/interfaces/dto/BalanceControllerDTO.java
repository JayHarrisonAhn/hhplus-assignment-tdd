package com.example.concert.interfaces.dto;

import com.example.concert.interfaces.dto.entity.BalanceHistoryDTO;
import lombok.Builder;
import lombok.Getter;

public class BalanceControllerDTO {

    public static class Charge {
        @Getter public static class Request {
            Long userId;
            Long amount;
        }
        @Builder @Getter public static class Response {
            BalanceHistoryDTO balanceHistory;
        }
    }
}
