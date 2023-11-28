package com.barbet.howtodobe.domain.category.domain;

import antlr.collections.impl.BitSet;
import com.barbet.howtodobe.global.common.BaseTimeEntity;
import com.barbet.howtodobe.domain.member.domain.Member;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Builder
@AllArgsConstructor
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
    private Integer week;

    @Column(nullable = false)
    private Integer month;

//    @Builder
//    public Category(Member member) {
//        this.member = member;
//        this.name = this.getName();
//        this.week = this.calculateWeek();
//        this.month = this.calculateMonth();
//    }
}