package com.example.concert.balance.api;

import com.example.concert.balance.api.dto.BalanceHistoryDTO;
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
