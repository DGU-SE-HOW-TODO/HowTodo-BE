package com.barbet.howtodobe.domain.failtag.dto;

import com.barbet.howtodobe.domain.failtag.domain.AllFailtag;
import com.barbet.howtodobe.domain.failtag.domain.Failtag;
import com.barbet.howtodobe.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

@Data
@AllArgsConstructor
public class FailtagRequestDTO {

    private String selectedDate;
    private List<String> selectedFailtagList;

    public Failtag toEntity (Member member) {
        LocalDate date = LocalDate.parse(
                this.selectedDate, DateTimeFormatter.ISO_DATE);
        Integer woy = date.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
        String[] datechunks = selectedDate.split("-");
        int year= Integer.parseInt(datechunks[0]);
        int month = Integer.parseInt(datechunks[1]);
        int week = woy;
        return Failtag.builder()
                .member(member)
                .year(year)
                .month(month)
                .week(week)
                .selectedFailtagList(selectedFailtagList)
                .build();
    }
}
