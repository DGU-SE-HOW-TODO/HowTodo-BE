package com.barbet.howtodobe.domain.member.dto;

import com.barbet.howtodobe.domain.member.domain.Member;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpRequestDTO {

    private String email;
    private String password;
    private String nickname;

    @Builder
    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();
    }
}
