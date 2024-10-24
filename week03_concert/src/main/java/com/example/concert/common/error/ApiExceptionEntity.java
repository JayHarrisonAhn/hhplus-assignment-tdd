package com.example.concert.common.error;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiExceptionEntity {

    private String errorCode;
    private String errorMessage;
}
