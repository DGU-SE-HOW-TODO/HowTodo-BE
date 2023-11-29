package com.barbet.howtodobe.domain.todo.dao;

import com.barbet.howtodobe.domain.calendar.domain.Calendar;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query(value = "SELECT COUNT(t) FROM Todo t " +
            "WHERE t.calendar.calendarId = :calendar_id")
    int getTotalTodoCnt(@Param("calendar_id") Long calendarId);

    @Query(value = "SELECT COUNT(t) FROM Todo t " +
            "WHERE t.calendar.calendarId = :calendar_id " +
            "AND t.isChecked = true")
    int getSuccessedTodoCnt(@Param("calendar_id") Long calendarId);

    // calendar, category, name 비교
    @Query(value = "SELECT t FROM Todo t " +
            "WHERE t.calendar = :calendar_id " +
            "and t.name = :name " +
            "and t.category = :category_id ")
    Optional<Todo> findByCalCatName(@Param("calendar_id") Long calendarId,
                                    @Param("category_id") Long categoryId,
                                    @Param("name") String name);

    @Query(value = "SELECT t FROM Todo t " +
            "WHERE t.todoId = :todo_id AND t.category.categoryId = :category_id")
    Optional<Todo> findByTodoCategoryId(@Param("todo_id") Long todoId,
                                        @Param("category_id") Long categoryId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Todo t SET t.isFixed = :is_fixed " +
            "WHERE t.todoId = :todo_id AND t.category.categoryId = :category_id")
    void updateIsFixed(@Param("is_fixed") boolean isFixed,
                       @Param("todo_id") Long todoId,
                       @Param("category_id") Long categoryId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Todo t SET t.isChecked = :is_checked " +
            "WHERE t.todoId = :todo_id AND t.category.categoryId = :category_id")
    void updateIsChecked(@Param("is_checked") boolean isChecked,
                         @Param("todo_id") Long todoId,
                         @Param("category_id") Long categoryId);



    /** for STATISTIC */
    // 투두 달성률 (전체)
    @Query("SELECT t FROM Todo t " +
            "JOIN FETCH t.member m " +
            "WHERE YEAR(t.createdDate) = :year " +
            "AND MONTH(t.createdDate) = :month " +
            "AND t.week = :week")
    List<Todo> todoForStatistic (@Param("year") Integer year,
                                 @Param("month") Integer month,
                                 @Param("week") Integer week);

    // 투두 달성률 (달성한 것)
    @Query("SELECT t FROM Todo t " +
            "JOIN FETCH t.member m " +
            "WHERE YEAR(t.createdDate) = :year " +
            "AND MONTH(t.createdDate) = :month " +
            "AND t.week = :week " +
            "AND t.isChecked = true ")
    List<Todo> todoForStatisticByIsCheckedTrue (@Param("year") Integer year,
                                                @Param("month") Integer month,
                                                @Param("week") Integer week);

    // selectedDate에 해당하는 카테고리의 투두 정보
    @Query("SELECT t FROM Todo t " +
            "JOIN FETCH t.member m " +
            "WHERE YEAR(t.createdDate) = :year " +
            "AND MONTH(t.createdDate) = :month " +
            "AND t.week = :week " +
            "AND t.category = :categoryId")
    List<Todo> categoryForStatistic (@Param("year") Integer year,
                                     @Param("month") Integer month,
                                     @Param("week") Integer week,
                                     @Param("categoryId") Long categoryId);


    /** for FEEDBACK */
    // 미룬 투두 정보
    @Query("SELECT t FROM Todo t " +
            "JOIN FETCH t.member m " +
            "WHERE YEAR(t.createdDate) = :year " +
            "AND MONTH(t.createdDate) = :month " +
            "AND t.week = :week " +
            "AND t.isDelay = true ")
    List<Todo> todoForFeedbackByIsDelayTrue (@Param("year") Integer year,
                                             @Param("month") Integer month,
                                             @Param("week") Integer week);
}