package com.example.concert.common.config;

import com.example.concert.common.api.LoggerFilter;
import com.example.concert.common.api.TokenValidateFilter;
import com.example.concert.common.api.TokenValidateInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class ApiConfig implements WebMvcConfigurer {

    private final TokenValidateInterceptor tokenValidateInterceptor;

    @Bean
    public FilterRegistrationBean<TokenValidateFilter> tokenValidateFilter() {
        FilterRegistrationBean<TokenValidateFilter> tokenValidateFilterFilterRegistrationBean = new FilterRegistrationBean<>();
        tokenValidateFilterFilterRegistrationBean.setFilter(new TokenValidateFilter());
        tokenValidateFilterFilterRegistrationBean.setOrder(2);
        tokenValidateFilterFilterRegistrationBean.addUrlPatterns("/concert/*");
        return tokenValidateFilterFilterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<LoggerFilter> loggerFilter() {
        FilterRegistrationBean<LoggerFilter> logFilterFilterRegistrationBean = new FilterRegistrationBean<>();
        logFilterFilterRegistrationBean.setFilter(new LoggerFilter());
        logFilterFilterRegistrationBean.setOrder(1);
        logFilterFilterRegistrationBean.addUrlPatterns("*");
        return logFilterFilterRegistrationBean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenValidateInterceptor)
                .addPathPatterns("/concert/**");
    }
}
