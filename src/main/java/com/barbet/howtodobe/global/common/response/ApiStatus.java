package com.barbet.howtodobe.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiStatus {
    private String statusCode;
    private String statusCodeMessage;
    private String statusMessage;

    public ApiStatus(HowTodoStatus howTodoStatus, String statusMessage) {
        this.statusCode = howTodoStatus.getErrorCode();
        this.statusCodeMessage = howTodoStatus.getErrorMessage();
        this.statusMessage = statusMessage;
    }
}
