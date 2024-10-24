package com.example.concert.common.error;

import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {
    private final CommonErrorCode commonErrorCode;

    public CommonException(CommonErrorCode commonErrorCode) {
        super(commonErrorCode.getMessage());
        this.commonErrorCode = commonErrorCode;
    }
}
