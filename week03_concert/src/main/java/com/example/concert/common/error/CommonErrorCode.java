package com.example.concert.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CommonErrorCode {
    TOKEN_NOT_VALID(HttpStatus.UNAUTHORIZED, "TE001", "Token is not valid"),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "UE001", "User not found"),
    USER_PARAM_INSUFFICIENT(HttpStatus.BAD_REQUEST, "UE001", "User param insufficient"),

    CONCERT_NOT_FOUND(HttpStatus.NOT_FOUND, "CE001", "Concert not found"),
    CONCERT_TIMESLOT_NOT_FOUND(HttpStatus.NOT_FOUND, "CE002", "Concert timeslot not found"),
    CONCERT_SEAT_NOT_FOUND(HttpStatus.NOT_FOUND, "CE003", "Concert seat not found"),
    CONCERT_SEAT_ALREADY_OCCUPIED(HttpStatus.CONFLICT, "CE004", "Concert seat is already occupied by someone"),

    BALANCE_INSUFFICIENT(HttpStatus.BAD_REQUEST, "BE001", "Balance insufficient"),
    BALANCE_TRANSACTION_AMOUNT_LESS_THAN_ZERO(HttpStatus.BAD_REQUEST, "BE002", "Balance transaction amount less than zero"),

    UNHANDLED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E001", "Unhandled error"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    CommonErrorCode(HttpStatus httpStatus, String code) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = "No msg";
    }

    CommonErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
