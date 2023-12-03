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
@Builder
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

    @Column(name = "is_checked", nullable = false)
    @ColumnDefault("false")
    private Boolean isChecked;

    @Column(name = "is_fixed", nullable = false)
    @ColumnDefault("false")
    private Boolean isFixed;

    @Column(name = "is_delay", nullable = false)
    @ColumnDefault("false")
    private Boolean isDelay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", nullable = false)
    private Member member;

    @Column(nullable = true)
    private String failtagName;

    /** 투두 주차만 따로 컬럼으로 설정
     * : 투두 관련 쿼리 메서드가 없음
     */
    private Integer week;

    public Todo(Calendar calendar, Member member, Category category, String name, String priority){
        this.calendar = calendar;
        this.member = member;
        this.category = category;
        this.name = name;
        this.priority = priority;
    }

    public void updateTodoWithFailtag (String failtagName,
                                       Boolean isDelay,
                                       Boolean isChecked) {
        this.failtagName = failtagName;
        this.isDelay = isDelay;
        this.isChecked = isChecked;
    }

    public void updateTodoChecked (Boolean isChecked, Member member) {
        this.isChecked = isChecked;
        this.member = member;
    }
    public void updateTodoFixed (Boolean isFixed, Member member) {
        this.isFixed = isFixed;
        this.member = member;
    }

    public void updateTodoDelay (Boolean isDelay, Member member) {
        this.isDelay = isDelay;
        this.member = member;
    }
}