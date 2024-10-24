package com.example.concert.token.api;

import com.example.concert.token.TokenFacade;
import com.example.concert.token.domain.Token;
import com.example.concert.token.api.TokenControllerDTO.*;
import com.example.concert.token.api.dto.TokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
public class TokenController {

    private final TokenFacade tokenFacade;

    @PostMapping("")
    @Operation(summary = "토큰 발급", description = "Concert API에 접근하기 위한 토큰을 발급합니다.")
    Issue.Response issue(
            Issue.Request request
    ) {
        Token token = this.tokenFacade.issue(request.getUserId());
        return Issue.Response.builder()
                .token(TokenDTO.from(token))
                .build();
    }

    @GetMapping("")
    @Operation(summary = "토큰 확인", description = "Concert API에 접근하기 위한 토큰이 유효한지 확인합니다.")
    Check.Response check(
            Check.Request request
    ) {
        Token token = this.tokenFacade.check(
                request.getUserId(),
                request.getToken()
        );
        return Check.Response.builder()
                .token(TokenDTO.from(token))
                .build();
    }
}