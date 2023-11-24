package com.barbet.howtodobe.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CustomErrorCode {

    /** ErrorCode 각자 추가해서 사용하기 */
    // Common (1xxx)
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 1001, "서버 내부에 오류가 있습니다."),

    // Member (2xxx)
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 2001, "사용자를 찾을 수 없습니다.");

    // Category (3xxx)

    // To Do (4xxx)

    // Failtag (5xxx)

    // Statistics / Feedback (6xxx)

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}
