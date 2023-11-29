package com.barbet.howtodobe.domain.feedback.dto;

import lombok.Getter;

@Getter
public class FeekbackResponseDTO {

    private String rateMessage;
    private String rateDetailMessage;

    private Integer firstPriPercent;
    private Integer secondPriPercent;
    private Integer thirdPriPercent;
    private String priorityMessage;
    private String priorityDetailMessage;

    private String delayMessage;
    private String delayDetailMessage;

    public FeekbackResponseDTO(String rateMessage,
                               String rateDetailMessage,
                               Integer firstPriPercent,
                               Integer secondPriPercent,
                               Integer thirdPriPercent,
                               String priorityMessage,
                               String priorityDetailMessage,
                               String delayMessage,
                               String delayDetailMessage) {
        this.rateMessage = rateMessage;
        this.rateDetailMessage = rateDetailMessage;
        this.firstPriPercent = firstPriPercent;
        this.secondPriPercent = secondPriPercent;
        this.thirdPriPercent = thirdPriPercent;
        this.priorityMessage = priorityMessage;
        this.priorityDetailMessage = priorityDetailMessage;
        this.delayMessage = delayMessage;
        this.delayDetailMessage = delayDetailMessage;
    }
}
