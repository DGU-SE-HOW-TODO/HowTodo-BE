package com.barbet.howtodobe.domain.todo.domain;

import com.barbet.howtodobe.domain.calendar.domain.Calendar;
import com.barbet.howtodobe.domain.category.domain.Category;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.statistic.domain.Statistic;
import com.barbet.howtodobe.global.common.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TODO")
public class Todo extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calender_id")
    private Calendar calendar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String priority;

    @Column(name = "is_checked", nullable = true)
    @ColumnDefault("false")
    private boolean isChecked;

    @Column(name = "is_fixed", nullable = true)
    @ColumnDefault("false")
    private boolean isFixed;

    @Column(name = "is_delay", nullable = true)
    @ColumnDefault("false")
    private boolean isDelay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="statistics_id", nullable = false)
    private Statistic statistics;

    /** 투두 주차만 따로 컬럼으로 설정
     * : 투두 관련 쿼리 메서드가 없음
     */
    private Integer week;

    @Builder
    public Todo(Calendar calendar, Category category, String name, String priority){
        this.calendar = calendar;
        this.category = category;
        this.name = name;
        this.priority = priority;
    }

}