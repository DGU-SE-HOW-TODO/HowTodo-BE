package com.barbet.howtodobe.domain.statistics.domain;

import com.barbet.howtodobe.domain.category.domain.Category;
import com.barbet.howtodobe.domain.failtag.domain.Failtag;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Statistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer week;
    private Integer month;
    private LocalDate selectedDate;

    @OneToMany(mappedBy = "statistics", cascade = CascadeType.ALL)
    private List<Failtag> failtagList = new ArrayList<>();

    @OneToMany(mappedBy = "statistics", cascade = CascadeType.ALL)
    private List<Todo> todoList = new ArrayList<>();

    @OneToMany(mappedBy = "statistics", cascade = CascadeType.ALL)
    private List<Category> categoryList = new ArrayList<>();

//    private Long weekFailtagId;
//    private Long weekCategoryId;
//    private Long weekAchievementId;
}
