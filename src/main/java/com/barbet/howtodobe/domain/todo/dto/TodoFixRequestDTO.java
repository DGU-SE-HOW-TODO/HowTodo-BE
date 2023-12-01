package com.barbet.howtodobe.domain.todo.dto;

import lombok.Data;

@Data
public class TodoFixRequestDTO {
    String selectedDate;
    Long todoCategoryId;
    Long todoId;
    Boolean isFixed;
}
