package com.barbet.howtodobe.domain.statistic.domain;

import com.barbet.howtodobe.domain.category.domain.Category;
import com.barbet.howtodobe.domain.failtag.domain.Failtag;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Statistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer year;
    private Integer month;
    private Integer week;

    // private LocalDate selectedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;


    // TODO 일단 하나에 작성하고 나중에 리팩토링
    @OneToMany(mappedBy = "statistics", cascade = CascadeType.ALL)
    private List<Todo> todoList = new ArrayList<>();
    @OneToMany(mappedBy = "statistics", cascade = CascadeType.ALL)
    private List<Category> categoryList = new ArrayList<>();
    @OneToMany(mappedBy = "statistics", cascade = CascadeType.ALL)
    private List<Failtag> failtagList = new ArrayList<>();


    /** 투두 통계 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id", nullable = false)
    private Todo todo;

    private Integer prevTodoCnt;
    private Integer prevTodoDoneCnt;
    private Integer nowTodoCnt;
    private Integer nowTodoDoneCnt;
    private Integer rateOfChange;
}
