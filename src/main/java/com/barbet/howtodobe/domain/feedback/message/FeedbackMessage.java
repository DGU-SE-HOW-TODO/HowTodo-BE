package com.barbet.howtodobe.domain.feedback.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FeedbackMessage {

    /** 달성률 피드백 메시지 */
    INCREASED_RATE_MESSAGE("저번 주에 비해 목표 달성률이 %d% 올랐지만, 여전히 목표 달성률의 절반을 넘지 못했어요."),
    DECREASED_RATE_MESSAGE("저번 주에 비해 목표 달성률이 %d%만큼 떨어졌고, 목표 달성률의 절반을 넘지 못했어요."),


    /** 우선순위 피드백 메시지 */


    /** 미루기 피드백 메시지 */
    DELAY_MESSAGE("이번주에는 전체 계획 중 미룬 계획이 %d개 있어요." +
            "가장 자주 미룬 카테고리는 %s 이에요.");


    private final String message;
    public String format(Object ... args) {
        return (message != null) ? String.format(message, args) : null;
    }
}
