package com.barbet.howtodobe.global.common.response;

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
        message.setApiStatus(new ApiStatus(HowTodoStatus.OK, null));
        return message;
    }
}
