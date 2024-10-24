package com.example.concert.balance.api;

import com.example.concert.balance.api.dto.BalanceHistoryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.beans.ConstructorProperties;

public class BalanceControllerDTO {

    public static class Charge {
        @Getter @AllArgsConstructor @NoArgsConstructor
        public static class Request {
            Long amount;
        }
        @Getter @Builder public static class Response {
            BalanceHistoryDTO balanceHistory;
        }
    }
}
