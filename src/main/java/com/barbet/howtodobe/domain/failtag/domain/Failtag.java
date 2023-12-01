package com.barbet.howtodobe.domain.failtag.domain;

import com.barbet.howtodobe.global.common.BaseTimeEntity;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
public class Failtag extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long failtagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer week;

    @Column(nullable = false)
    private Integer month;

    @ElementCollection
    @CollectionTable(name = "selected_failtag", joinColumns = @JoinColumn(name = "failtag_id"))
    @Column(name = "failtag_name")
    private List<String> selectedFailtagList;

    @Builder
    public Failtag(Member member,
                   Integer year,
                   Integer month,
                   Integer week,
                   String name,
                   List<String> selectedFailtagList) {
        this.member = member;
        this.year = year;
        this.month = month;
        this.week = week;
        this.name = name;
        this.selectedFailtagList = selectedFailtagList;
    }
}
