package com.example.concert.interfaces.controller;

import com.example.concert.interfaces.dto.TokenControllerDTO.*;
import com.example.concert.interfaces.dto.entity.TokenDTO;
import com.example.concert.interfaces.dto.entity.TokenStatusDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
public class TokenController {

    @PostMapping("")
    @Operation(summary = "토큰 발급", description = "Concert API에 접근하기 위한 토큰을 발급합니다.")
    Issue.Response issue(
            Issue.Request request
    ) {
        return Issue.Response.builder()
                .token(
                        TokenDTO.builder()
                                .id(UUID.randomUUID().toString())
                                .status(TokenStatusDTO.WAIT)
                                .build()
                )
                .build();
    }

    @GetMapping("")
    @Operation(summary = "토큰 확인", description = "Concert API에 접근하기 위한 토큰이 유효한지 확인합니다.")
    Check.Response check(
            Check.Request request
    ) {
        return Check.Response.builder()
                .token(
                        TokenDTO.builder()
                                .id(request.getToken())
                                .status(TokenStatusDTO.ACTIVE)
                                .build()
                )
                .build();
    }
}
