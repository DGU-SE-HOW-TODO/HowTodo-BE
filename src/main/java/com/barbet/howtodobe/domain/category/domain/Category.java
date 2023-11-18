package com.barbet.howtodobe.domain.category.domain;

import com.barbet.howtodobe.global.common.BaseTimeEntity;
import com.barbet.howtodobe.domain.member.domain.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "CATEGORY")
@EqualsAndHashCode(callSuper = true)
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

    @Builder
    public Category(Member member, String name) {
        this.member = member;
        this.name = name;
        this.week = this.calculateWeek();
    }

}
