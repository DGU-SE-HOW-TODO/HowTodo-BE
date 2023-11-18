package com.barbet.howtodobe.domain.category.domain;

import com.barbet.howtodobe.domain.BaseTimeEntity;
import com.barbet.howtodobe.domain.member.domain.Member;
import lombok.*;

import javax.persistence.*;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CATEGORY")
@Getter
@Setter
@Builder(builderMethodName = "CategoryBuilder")
public class Category extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int week;

    private void calculateWeek() {
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        int weekNumber = this.getCreatedDate().get(woy);
        int month = this.getCreatedDate().getMonthValue();

        // 이슈: 월도 같이 계산해서 넣어주는게 좋을까
        System.out.println(month + "월 " + weekNumber + "주차");
        this.week = weekNumber;
    }

    public CategoryBuilder builder(Member member, String name){
        this.calculateWeek();
        return CategoryBuilder().member(member).name(name).week(week);
    }
}
