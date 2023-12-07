package com.barbet.howtodobe.global.common.response;

import com.barbet.howtodobe.global.common.exception.CustomResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiStatus {
    private int statusCode;
    private String statusCodeMessage;
    private String statusMessage;

    public ApiStatus(CustomResponseCode howTodoStatus, String statusMessage) {
        this.statusCode = howTodoStatus.getCode();
        this.statusCodeMessage = howTodoStatus.getMessage();
        this.statusMessage = statusMessage;
    }
}
