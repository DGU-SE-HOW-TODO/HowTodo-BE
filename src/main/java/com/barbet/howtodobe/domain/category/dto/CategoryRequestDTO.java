package com.barbet.howtodobe.domain.category.dto;

import com.barbet.howtodobe.domain.category.domain.Category;
import com.barbet.howtodobe.domain.member.domain.Member;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CategoryRequestDTO {

    private String name;

    public Category toEntity (Member member) {
        return Category.builder()
                .member(member)
                .name(name)
                .build();
    }
}