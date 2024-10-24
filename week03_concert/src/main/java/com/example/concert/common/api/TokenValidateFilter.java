package com.example.concert.common.api;

import com.example.concert.common.error.ApiExceptionEntity;
import com.example.concert.common.error.CommonErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class TokenValidateFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {
        final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        final HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        try {
            Long.valueOf(httpServletRequest.getHeader("Authorization"));
            httpServletRequest.getHeader("Token");
            filterChain.doFilter(servletRequest, servletResponse);
        } catch(Exception e) {
            setTokenNotValidResponse(httpServletResponse);
        }
    }

    private void setTokenNotValidResponse(HttpServletResponse httpServletResponse) {
        httpServletResponse.setStatus(CommonErrorCode.TOKEN_NOT_VALID.getHttpStatus().value());
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        try {
            String json = new ObjectMapper().writeValueAsString(
                    ApiExceptionEntity.builder()
                            .errorCode(CommonErrorCode.TOKEN_NOT_VALID.getCode())
                            .errorMessage(CommonErrorCode.TOKEN_NOT_VALID.getMessage())
                            .build()
            );
            httpServletResponse.getWriter().write(json);
        } catch (Exception jsonE) {
            log.error(jsonE.getMessage());
        }
    }
}
