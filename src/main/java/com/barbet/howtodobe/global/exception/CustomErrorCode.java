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
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 2001, "사용자를 찾을 수 없습니다."),

    // Category (3xxx)

    // To Do (4xxx)
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, 4001, "해당하는 To do가 없습니다."),

    // Failtag (5xxx)
    FAILTAG_COUNT_IS_NOT_FIVE(HttpStatus.BAD_REQUEST, 5001, "선택된 실패태그는 5개이여야 합니다."),
    NOT_EXIST_WEEK_FAILTAG(HttpStatus.BAD_REQUEST, 5002, "이번주에 등록된 실패태그가 없습니다."),
    INVALID_FAILTAG(HttpStatus.BAD_REQUEST, 5003, "잘못 선택된 실패태그 입니다. 이번주에 등록된 실패태그만 선택해주세요");

    // Statistics / Feedback (6xxx)

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}
