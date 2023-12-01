package com.barbet.howtodobe.domain.todo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TodoCheckRequestDTO {
    String selectedDate;
    Long todoCategoryId;
    Long todoId;
    Boolean isChecked;
}
