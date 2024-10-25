package com.example.concert.balance.api;

import com.example.concert.balance.BalanceFacade;
import com.example.concert.balance.domain.balancehistory.BalanceHistory;
import com.example.concert.balance.api.BalanceControllerDTO.*;
import com.example.concert.balance.api.dto.BalanceHistoryDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pay")
public class BalanceController {

    private final BalanceFacade balanceFacade;

    @Operation(summary = "잔액 충전", description = "잔액을 충전합니다.")
    @PostMapping("/charge")
     Charge.Response charge(
             @RequestHeader("Authorization") Long userId,
             @RequestBody Charge.Request request
    ) {
        BalanceHistory balanceHistory = balanceFacade.charge(
                userId,
                request.getAmount()
        );
        return Charge.Response.builder()
                .balanceHistory(
                        BalanceHistoryDTO.from(balanceHistory)
                )
                .build();
    }
}
