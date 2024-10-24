package com.example.concert.common.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ApiControllerAdvice {

    @ExceptionHandler(CommonException.class)
    protected ResponseEntity<ApiExceptionEntity> exceptionHandler(final CommonException e) {
        log.info(e.getMessage());
        return ResponseEntity
                .status(e.getCommonErrorCode().getHttpStatus())
                .body(ApiExceptionEntity.of(e.getCommonErrorCode()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiExceptionEntity> unhandledExceptionHandler(final Exception e) {
        ResponseEntity<ApiExceptionEntity> responseEntity = this.exceptionHandler(
                new CommonException(CommonErrorCode.UNHANDLED_ERROR)
        );
        log.error(e.getMessage(), e);
        return responseEntity;
    }
}
