package com.barbet.howtodobe.domain.member.dto;

import com.barbet.howtodobe.domain.member.domain.Member;
import lombok.Builder;
import lombok.Data;

@Data
public class SignInRequestDTO {
    private String email;
    private String password;

    @Builder
    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .build();
    }
}
