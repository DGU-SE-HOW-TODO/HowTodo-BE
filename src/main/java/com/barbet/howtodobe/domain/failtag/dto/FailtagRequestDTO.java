package com.barbet.howtodobe.domain.failtag.dto;

import com.barbet.howtodobe.domain.failtag.domain.AllFailtag;
import com.barbet.howtodobe.domain.failtag.domain.Failtag;
import com.barbet.howtodobe.domain.member.domain.Member;
import lombok.Getter;

import java.util.List;

@Getter
public class FailtagRequestDTO {

    private Integer year;
    private Integer month;
    private Integer week;
    private List<String> selectedFailtagList;
    public Failtag toEntity (Member member) {
        return Failtag.builder()
                .member(member)
                .year(year)
                .month(month)
                .week(week)
                .selectedFailtagList(selectedFailtagList)
                .build();
    }
}
