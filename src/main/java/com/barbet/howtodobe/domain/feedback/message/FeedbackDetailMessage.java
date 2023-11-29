package com.barbet.howtodobe.domain.feedback.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FeedbackDetailMessage {

    /** 달성률 세부 피드벡 메시지 */

    /** 우선순위 세부 피드백 메시지 */

    /** 미루기 세부 피드백 메시지 */
    DELAY_1_5("다음주에는 미룬 계획이 %d개 이하가 되도록 열심히 해보는 것은 어떨까요?"),
    DELAY_6("미루는 습관을 극복하려면 때로는 %s이 도움이 된다고 해요.");

    private final String detailMessageFormat;

    public String format(Object ... args) {
        return (detailMessageFormat != null) ? String.format(detailMessageFormat, args) : null;
    }
}
