package com.barbet.howtodobe.domain.statistics.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Statistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer prevTodoCnt;
    private Integer prevTodoDoneCnt;
    private Integer nowTodoCnt;
    private Integer nowTodoDoneCnt;
    private Integer rateOfChange;
    private String nowBestCategory;
    private String nowWorstFailTag;

    // 대분류 같은 경우는 enum 타입으로 설정해둬야 하는거 아닌가
}
