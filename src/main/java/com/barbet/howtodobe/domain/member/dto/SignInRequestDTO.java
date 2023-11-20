package com.barbet.howtodobe.domain.member.dto;

import lombok.Data;

@Data
public class SignInRequestDTO {
    private String email;
    private String password;
}
