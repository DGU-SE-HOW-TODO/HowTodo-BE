package com.barbet.howtodobe.domain.todo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TodoCheckResponseDTO {
    @JsonProperty("isChecked")
    Boolean isChecked;
}
