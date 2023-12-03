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
    EMAIL_ALREADY_EXIST(HttpStatus.NOT_FOUND, 2002, "이미 존재하는 이메일입니다."),
    NOT_EXIST_PASSWORD(HttpStatus.NOT_FOUND, 2003, "비밀번호가 입력되지 않았습니다"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, 2004, "토큰이 만료되었습니다."),
    INVALID_TOKEN_FORMAT(HttpStatus.BAD_REQUEST, 2005, "잘못된 토큰 형식입니다."),
    TOKEN_VALIDATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 2006, "토큰 검증 중 오류가 발생했습니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, 2007, "잘못된 비밀번호입니다."),

    // Category (3xxx)
    NOT_EXIST_WEEK_CATEGORY(HttpStatus.BAD_REQUEST, 3001, "이번주에 관한 카테고리가 없습니다."),

    // To Do (4xxx)
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, 4001, "해당하는 To do가 없습니다."),
    CAN_NOT_TODO_CHECK(HttpStatus.NOT_FOUND, 4002, "이미 미룬 To do는 체크할 수 없습니다."),

    // Failtag (5xxx)
    FAILTAG_COUNT_IS_NOT_FIVE(HttpStatus.BAD_REQUEST, 5001, "선택된 실패태그는 5개이여야 합니다."),
    NOT_EXIST_WEEK_FAILTAG(HttpStatus.BAD_REQUEST, 5002, "이번주에 등록된 실패태그가 없습니다."),
    INVALID_FAILTAG(HttpStatus.BAD_REQUEST, 5003, "잘못 선택된 실패태그 입니다. 이번주에 등록된 실패태그만 선택해주세요"),

    // Statistics (6xxx)
    NOT_EXIST_STATISTICS_INFO(HttpStatus.NOT_FOUND, 6001, "이번주에 통계 정보가 없습니다."),

    // Calendar (7xxx)
    NOT_EXIST_CALENDAR(HttpStatus.NOT_FOUND, 7001, "해당 날짜가 캘린더에 존재하지 않습니다");


    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}
