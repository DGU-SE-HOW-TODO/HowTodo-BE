package com.barbet.howtodobe.domain.todo.domain;

import com.barbet.howtodobe.domain.calendar.domain.Calendar;
import com.barbet.howtodobe.domain.category.domain.Category;
import com.barbet.howtodobe.domain.failtag.domain.Failtag;
import com.barbet.howtodobe.domain.member.domain.Member;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TODO")
public class Todo { // Date 값은 클라이언트로부터 받아야 해서 BaseTimeEntity X

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calender_id")
    private Calendar calendar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "failtag_id")
    private Failtag failtag;

    @Column(nullable = false)
    private String name;

    private LocalDate selectedDate;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean isChecked;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean isDelay;
}
