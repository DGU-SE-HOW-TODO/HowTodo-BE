package com.barbet.howtodobe.domain.feedback.message;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RandomDelayTips {

    /** [미루는 습관을 극복하는 팁] 랜덤 메시지 */

    MESSAGE_01("생각을 줄이고 지금 당장 시작하는 것"),
    MESSAGE_02("할 기분이 아니더라도 일단 시작하는 것"),
    MESSAGE_03("할 일을 잘게 쪼개서 작고 잦는 성공을 반복하는 것"),
    MESSAGE_04("소요 시간에 대한 정확한 예측력을 기르는 것"),
    MESSAGE_05("일의 우선순위를 알아보는 것"),
    MESSAGE_06("두루뭉술한 목표보다는 구체적이로 명확한 목표를 세우는 것"),
    MESSAGE_07("미루더라도 자신을 비난하지 않는 것"),
    MESSAGE_08("일의 마무리가 완벽하지 않을 수도 있다는 사실을 인지하는 것"),
    MESSAGE_09("시간이 지나더라도 할 일이 쉬워지거나 즐거워지지 않는다는 사실을 인지하는 것");

    private final String message;

}
