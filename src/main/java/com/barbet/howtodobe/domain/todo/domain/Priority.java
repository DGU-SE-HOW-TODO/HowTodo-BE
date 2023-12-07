package com.barbet.howtodobe.domain.todo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Priority {
    VERY_IMPORTANT(1,"매우 중요"),
    IMPORTANT(2,"중요"),
    NOT_IMPORTANT(3,"중요하지 않음");

    private int index;
    private String priority;
}
