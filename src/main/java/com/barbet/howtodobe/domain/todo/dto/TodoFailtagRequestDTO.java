package com.barbet.howtodobe.domain.todo.dto;

import com.barbet.howtodobe.domain.todo.domain.Todo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodoFailtagRequestDTO {

    private String failTagName;
    private Boolean isDelay;

    @Builder
    public Todo toEntity() {
        return Todo.builder()
                .failtagName(failTagName)
                .isDelay(isDelay)
                .build();
    }
}
