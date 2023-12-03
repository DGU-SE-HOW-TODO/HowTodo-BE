package com.barbet.howtodobe.global.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse {

    private final int errorCode;
    private final String message;
}
