package com.barbet.howtodobe.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // CustomErrorCode기반 ResponseEntity 생성
    private ResponseEntity<Object> handleExceptionInternal(CustomErrorCode customErrorCode, String errorMessage) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(customErrorCode.getCode())
                .message(errorMessage)
                .build();

        return ResponseEntity
                .status(customErrorCode.getHttpStatus())
                .body(errorResponse);
    }

    // Custion Exception 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException e, HttpServletRequest request) {
        log.error("(* error *) CustomException - url: {}, errorCode: {}, errorMessage: {}",
                request.getRequestURL(), e.getCustomErrorCode().getCode(), e.getMessage());
        return handleExceptionInternal(e.getCustomErrorCode(), e.getMessage());
    }
}
