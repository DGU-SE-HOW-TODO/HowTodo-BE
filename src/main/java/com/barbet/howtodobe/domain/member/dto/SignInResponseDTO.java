package com.barbet.howtodobe.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInResponseDTO {
    private String responseMessage;
    private String accessToken;

    public SignInResponseDTO (String accessToken) {
        this.accessToken = accessToken;
    }

}
