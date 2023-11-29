package com.barbet.howtodobe.domain.feedback.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FeedbackMessage {

    /** 달성률 피드백 메시지 */

    /** 우선순위 피드백 메시지 */

    /** 미루기 피드백 메시지 */
    DELAY_MESSAGE("이번주에는 전체 계획 중 미룬 계획이 %d개 있어요." +
            "가장 자주 미룬 카테고리는 %s 이에요.");

    private final String messageFormat;
    public String format(Object ... args) {
        return (messageFormat != null) ? String.format(messageFormat, args) : null;
    }
}
