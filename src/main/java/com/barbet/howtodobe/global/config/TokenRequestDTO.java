package com.barbet.howtodobe.global.config;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenRequestDTO {
    String accessToken;
    String refreshToken;

    @Builder
    public TokenRequestDTO (String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
