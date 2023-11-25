package com.barbet.howtodobe.domain.todo.domain;

import com.barbet.howtodobe.domain.calendar.domain.Calendar;
import com.barbet.howtodobe.domain.category.domain.Category;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TODO")
public class Todo { // Date 값은 클라이언트로부터 받아야 해서 BaseTimeEntity X
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

    @Column(name = "is_checked", nullable = true)
    @ColumnDefault("false")
    private boolean isChecked;

    @Column(name = "is_fixed", nullable = true)
    @ColumnDefault("false")
    private boolean isFixed;

    @Column(name = "is_delay", nullable = true)
    @ColumnDefault("false")
    private boolean isDelay;

    @Builder
    public Todo(Calendar calendar, Category category, String name, String priority){
        this.calendar = calendar;
        this.category = category;
        this.name = name;
        this.priority = priority;
    }

}
