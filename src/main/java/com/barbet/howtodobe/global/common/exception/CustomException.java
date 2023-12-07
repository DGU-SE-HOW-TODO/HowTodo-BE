package com.barbet.howtodobe.global.common.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final CustomResponseCode customErrorCode;
    private final String message;

    public CustomException(CustomResponseCode customErrorCode) {
        this.customErrorCode = customErrorCode;
        this.message = customErrorCode.getMessage();
    }

    public CustomException (CustomResponseCode customErrorCode, String message) {
        this.customErrorCode = customErrorCode;
        this.message = message;
    }
}
