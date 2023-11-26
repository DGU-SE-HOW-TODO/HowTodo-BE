package com.barbet.howtodobe.domain.todo.dto;

import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TodoRequestDTO {
    private String name;
    private LocalDate selectedDate;
    private Boolean isChecked;
    private Boolean isDelay;

    public Todo toEntity (Member member) {
        return Todo.builder()
                .member(member)
                .name(name)
                .selectedDate(selectedDate)
                .isChecked(isChecked)
                .isDelay(isDelay)
                .build();
    }
}
