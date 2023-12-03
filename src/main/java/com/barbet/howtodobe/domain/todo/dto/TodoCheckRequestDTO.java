package com.barbet.howtodobe.domain.todo.dto;

import lombok.Data;

@Data
public class TodoCheckRequestDTO {
    String selectedDate;
    Long todoCategoryId;
    Long todoId;
    Boolean isChecked;
}
