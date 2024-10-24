package com.example.concert.common.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(CommonException.class)
    protected ResponseEntity<ApiExceptionEntity> exceptionHandler(final CommonException e) {
        return ResponseEntity
                .status(e.getCommonErrorCode().getHttpStatus())
                .body(
                        ApiExceptionEntity.builder()
                                .errorCode(e.getCommonErrorCode().getCode())
                                .errorMessage(e.getCommonErrorCode().getMessage())
                                .build()
                );
    }
}
