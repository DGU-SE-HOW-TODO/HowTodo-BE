package com.barbet.howtodobe.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiStatus {
    private String errorCode;
    private String errorCodeMessage;
    private String errorMessage;

    public ApiStatus(HowTodoStatus howTodoStatus, String errorMessage) {
        this.errorCode = howTodoStatus.getErrorCode();
        this.errorCodeMessage = howTodoStatus.getErrorMessage();
        this.errorMessage = errorMessage;
    }
}
