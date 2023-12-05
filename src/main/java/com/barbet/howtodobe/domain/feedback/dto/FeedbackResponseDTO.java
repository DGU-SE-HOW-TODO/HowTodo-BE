package com.barbet.howtodobe.domain.feedback.dto;

import lombok.Getter;

@Getter
public class FeedbackResponseDTO {

    private Integer month;
    private Integer week;
    private String rateMessage;
    private String rateDetailMessage;

    private Integer firstPriPercent;
    private Integer secondPriPercent;
    private Integer thirdPriPercent;
    private String priorityMessage;
    private String priorityDetailMessage;

    private String delayMessage;
    private String delayDetailMessage;

    public FeedbackResponseDTO(Integer month,
            Integer week,
            String rateMessage,
                               String rateDetailMessage,
                               Integer firstPriPercent,
                               Integer secondPriPercent,
                               Integer thirdPriPercent,
                               String priorityMessage,
                               String priorityDetailMessage,
                               String delayMessage,
                               String delayDetailMessage) {
        this.month = month;
        this.week = week;
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
