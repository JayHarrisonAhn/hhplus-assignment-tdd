package com.example.concert.common.api;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
public class LoggerFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("Initializing Logging Filter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // 요청 로깅
        logRequest(httpServletRequest);

        chain.doFilter(request, response);

        // 응답 로깅
        logResponse(httpServletResponse);
    }

    private void logRequest(HttpServletRequest request) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("HTTP REQUEST: ")
                .append("[Method: ").append(request.getMethod()).append("] ")
                .append("[URI: ").append(request.getRequestURI()).append("] ")
                .append("[QueryString: ").append(request.getQueryString()).append("] ");

        log.info(logMessage.toString());
    }

    private void logResponse(HttpServletResponse response) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("HTTP RESPONSE: ")
                .append("[Status: ").append(response.getStatus()).append("]");

        log.info(logMessage.toString());
    }

    @Override
    public void destroy() {
        log.info("Destroying Logging Filter");
    }
}
