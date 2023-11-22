package com.barbet.howtodobe.domain.member.exception;

public class InvalidPassword extends RuntimeException{
    private final String errorMessage = "비밀번호에 해당하는 멤버가 존재하지 않습니다.";
}
