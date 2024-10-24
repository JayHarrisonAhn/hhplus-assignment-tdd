package com.example.concert.common.api;

import com.example.concert.token.TokenFacade;
import com.example.concert.token.domain.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class TokenValidateInterceptor implements HandlerInterceptor {

    private final TokenFacade tokenFacade;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long userId = Long.valueOf(request.getHeader("Authorization"));
        String tokenString = request.getHeader("Token");

        Token token = tokenFacade.check(userId, tokenString);
        token.validateUser(userId);
        return true;
    }
}
