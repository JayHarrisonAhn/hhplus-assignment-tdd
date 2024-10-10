package com.example.concert.interfaces.controller;

import com.example.concert.interfaces.dto.PayControllerDTO.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/pay")
public class PayController {

    @PostMapping("/charge")
     Charge.Response charge(
             Charge.Request request
    ) {
        return Charge.Response.builder()
                .balance(100000L)
                .build();
    }
}
