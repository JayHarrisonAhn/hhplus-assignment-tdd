package com.example.concert.common.error;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiExceptionEntity {

    private String errorCode;
    private String errorMessage;

    public static ApiExceptionEntity of(CommonErrorCode errorCode) {
        return ApiExceptionEntity.builder()
                .errorCode(errorCode.getCode())
                .errorMessage(errorCode.getMessage())
                .build();
    }
}
