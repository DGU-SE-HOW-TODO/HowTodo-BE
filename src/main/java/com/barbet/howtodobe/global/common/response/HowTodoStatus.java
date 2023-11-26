package com.barbet.howtodobe.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum HowTodoStatus {
    NOT_FOUND(404, "E000", "Page Not Found"),
    INTERNEL_SERVER_ERROR(500, "E001", "Internel Server Error"),
    INVALID_PARAMETER(400, "E002", "Invalid Parameter Value"),
    BAD_REQUEST(400, "E003", "Bad Request"),

    DUPLICATE_EMAIL(400, "E100", "Duplicate Email"),
    INVALID_PW(400, "E101", "Please Check Your Password"),

    TOKEN_INVALID(400, "E200", "Invalid Token"),

    OK(200, "N000", "Success")
    ;

    private int status;
    private String errorCode;
    private String errorMessage;
}
