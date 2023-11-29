package com.barbet.howtodobe.domain.feedback.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FeedbackDetailMessage {

    /** 달성률 세부 피드벡 메시지 */
    RATE_DETAIL_MESSAGE("이번 주 Todo 리스트를 보니, 아쉽게도 한 주 동안 목표의 절반을 달성하지 못했네요." +
            "욕심을 버리고 조금 더 현실적인 목표를 세워보는 것은 어떨까요? 목표를 세분화하여 계획하고, 이를 차근차근 달성하면 성취감을 느끼실 거예요. 목표치 만큼 Todo를 다 수행하지 못했다고 실망하지 말고 다음 주도 파이팅 해요!"),

    /** 우선순위 세부 피드백 메시지 */

    /** 미루기 세부 피드백 메시지 */
    DELAY_DETAIL_MESSAGE_1_5("다음주에는 미룬 계획이 %d개 이하가 되도록 열심히 해보는 것은 어떨까요?"),
    DELAY_DETAIL_MESSAGE_6("미루는 습관을 극복하려면 때로는 %s이 도움이 된다고 해요.");

    private final String detailMessage;

    public String format(Object ... args) {
        return (detailMessage != null) ? String.format(detailMessage, args) : null;
    }
}
