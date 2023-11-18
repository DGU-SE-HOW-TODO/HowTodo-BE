package com.barbet.howtodobe.domain.failtag.domain;

import com.barbet.howtodobe.global.common.BaseTimeEntity;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "FAILTAG")
@EqualsAndHashCode(callSuper = true)
public class Failtag extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long failtagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    private Todo todo;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int week;

    @Column(nullable = false)
    private boolean isSelected;

    @Builder
    public Failtag(Member member, String name, boolean isSelected){
        this.member = member;
        this.name = name;
        this.week = this.calculateWeek();
        this.isSelected = isSelected;
    }
}
