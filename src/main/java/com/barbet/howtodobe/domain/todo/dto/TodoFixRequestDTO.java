package com.barbet.howtodobe.domain.todo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TodoFixRequestDTO {
    String selectedDate;
    Long todoCategoryId;
    Long todoId;
    @JsonProperty("isFixed")
    boolean isFixed;
}
