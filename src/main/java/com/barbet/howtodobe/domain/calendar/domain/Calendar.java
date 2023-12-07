package com.barbet.howtodobe.domain.calendar.domain;

import com.barbet.howtodobe.global.common.BaseTimeEntity;
import com.barbet.howtodobe.domain.member.domain.Member;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "CALENDAR")
@EqualsAndHashCode(callSuper = true)
public class Calendar extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calendarId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private int successRate;

    @Column(nullable = false)
    private Date date;

    public Calendar(Member member, Date date){
        calculateSuccessRate();
        this.member = member;
        this.date = date;
    }

    private void calculateSuccessRate() { }
}
