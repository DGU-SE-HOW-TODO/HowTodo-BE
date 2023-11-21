package com.barbet.howtodobe.domain.statistics.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Statistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO 단방향 명시해주기 (양방향은?)
    private Long weekFailtagId;
    private Long weekCategoryId;
    private Long weekAchievementId;

    // TODO 몇 주차인지 로직 변경 필요
    private LocalDate selectedDate;
}
