package com.barbet.howtodobe.domain.nowCategory.dto;

import com.barbet.howtodobe.domain.nowCategory.domain.NowCategory;
import lombok.Getter;

@Getter
public class NowCategoryResponseDTO {

    private String nowCategory;
    private Integer nowCategoryRate;

    public NowCategory toEntity (String nowCategory, Integer nowCategoryRate) {
        return NowCategory.builder()
                .nowCategory(nowCategory)
                .nowCategoryRate(nowCategoryRate)
                .build();
    }
}