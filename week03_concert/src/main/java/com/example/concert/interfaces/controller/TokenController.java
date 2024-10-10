package com.example.concert.interfaces.controller;

import com.example.concert.interfaces.dto.TokenControllerDTO.*;
import com.example.concert.interfaces.dto.entity.TokenDTO;
import com.example.concert.interfaces.dto.entity.TokenStatusDTO;
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
