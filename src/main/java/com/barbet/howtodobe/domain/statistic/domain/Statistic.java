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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private Integer year;
    private Integer month;
    private Integer week;

    private Integer prevTodoCnt;
    private Integer prevTodoDoneCnt;
    private Integer nowTodoCnt;
    private Integer nowTodoDoneCnt;
    private Integer rateOfChange;

    private String nowBestCategory;
    private String nowWorstFailtag;
}
