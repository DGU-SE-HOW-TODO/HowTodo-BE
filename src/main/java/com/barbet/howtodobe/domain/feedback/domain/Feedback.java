package com.barbet.howtodobe.domain.feedback.domain;

import com.barbet.howtodobe.domain.member.domain.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer year;
    private Integer month;
    private Integer week;

    /** 달성률 피드백 */
    private String rateMessage;
    private String rateDetailMessage;

    /** 우선순위 피드백 */
    private Integer firstPriPercent;
    private Integer secondPriPercent;
    private Integer thirdPriPercent;
    private String priorityMessage;
    private String priorityDetailMessage;

    /** 미루기 피드백 */
    private String delayMessage;
    private String delayDetailMessage;
}
