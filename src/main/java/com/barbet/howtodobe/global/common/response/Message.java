package com.barbet.howtodobe.global.common.response;

import com.barbet.howtodobe.global.common.exception.CustomResponseCode;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
public class Message {
    private ApiStatus apiStatus;
    Object data;

    @Builder
    public Message(ApiStatus apiStatus, Object data){
        this.apiStatus = apiStatus;
        this.data = data;
    }

    public static Message createSuccessMessage(Object data){
        Message message = new Message();
        message.setData(data);
        message.setApiStatus(new ApiStatus(CustomResponseCode.SUCCESS, null));
        return message;
    }
}
