package com.barbet.howtodobe.domain.nowFailtag.dto;

import com.barbet.howtodobe.domain.nowFailtag.domain.NowFailtag;
import lombok.Getter;

@Getter
public class NowFailtagResponseDTO {

    private String nowFailtag;
    private Integer nowFailtagRate;

    public NowFailtag toEntity(String nowFailtag, Integer nowFailtagRate) {
        return NowFailtag.builder()
                .nowFailtag(nowFailtag)
                .nowFailtagRate(nowFailtagRate)
                .build();
    }
}
