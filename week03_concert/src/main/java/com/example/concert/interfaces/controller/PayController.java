package com.example.concert.interfaces.controller;

import com.example.concert.interfaces.dto.PayControllerDTO.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/pay")
public class PayController {

    @Operation(summary = "잔액 충전", description = "잔액을 충전합니다.")
    @PostMapping("/charge")
     Charge.Response charge(
             Charge.Request request
    ) {
        return Charge.Response.builder()
                .balance(100000L)
                .build();
    }
}
