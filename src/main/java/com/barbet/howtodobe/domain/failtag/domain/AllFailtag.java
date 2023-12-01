package com.barbet.howtodobe.domain.failtag.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AllFailtag {

    SLEEP(1, "잠"),
    BADBODY(2, "몸상태악화"),
    OTHERSCHE(3, "갑작스런일정"),
    NOWILL(4, "의지박약"),
    ONTIME(5, "정각병"),
    LOVESNS(6, "SNS"),
    LOVEGAME(7, "게임"),
    FAILMANAG(8, "시각관리실패"),
    OVERSCHE(9, "무리한계획"),
    LOWMOTIV(10, "낮은동기부여"),
    CHASCHE(11, "일정변경"),
    RELAX(12, "휴식"),
    SLUMP(13, "슬럼프"),
    LOWCONC(14, "집중력부족"),
    LOVEPHONE(15, "핸드폰중독"),
    HATETODO(16, "하기싫어"),
    BADCOND(17, "컨디션난조");

    private Integer index;
    private String failtagname;
}
