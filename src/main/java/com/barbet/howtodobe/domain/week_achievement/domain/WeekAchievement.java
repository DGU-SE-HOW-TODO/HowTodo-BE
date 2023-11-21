package com.barbet.howtodobe.domain.week_achievement.domain;

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
public class WeekAchievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer prevTodoCnt;
    private Integer prevTodoDoneCnt;
    private Integer nowTodoCnt;
    private Integer nowTodoDoneCnt;
    private Integer rateOfChange;
}
