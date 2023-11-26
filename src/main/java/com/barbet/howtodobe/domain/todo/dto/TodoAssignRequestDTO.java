package com.barbet.howtodobe.domain.todo.dto;

import lombok.Data;

@Data
public class TodoAssignRequestDTO {
    private String selectedDate;
    private Long todoCategoryId;
    private String todo;
    private String priority;
}
