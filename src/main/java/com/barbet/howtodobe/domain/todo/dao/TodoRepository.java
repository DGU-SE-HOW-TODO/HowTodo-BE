package com.barbet.howtodobe.domain.todo.dao;

import com.barbet.howtodobe.domain.calendar.domain.Calendar;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import org.hibernate.annotations.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
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

    /** for Statistic & Home Info */
    @Query("SELECT t FROM Todo t " +
            "JOIN FETCH t.member m " +
            "WHERE YEAR(t.createdDate) = :year " +
            "AND MONTH(t.createdDate) = :month " +
            "AND t.week = :week AND t.member.memberId = :memberId")
    List<Todo> findTodoBySelectedDate (@Param("year") Integer year,
                                 @Param("month") Integer month,
                                 @Param("week") Integer week,
                                       @Param("memberId") Long memberId);

    @Query("SELECT t FROM Todo t " +
            "WHERE t.calendar.calendarId = :calendarId")
    List<Todo> findHomeTodoByCalendarId(@Param("calendarId") Long calendarId);

    @Query("SELECT t FROM Todo t " +
            "WHERE t.calendar.calendarId = :calendarId " +
            "AND t.category.name = :categoryName AND t.member.memberId = :memberId")
    List<Todo> findHomeTodoByCalendarIdANDCategoryId(@Param("calendarId") Long calendarId,
                                                     @Param("categoryName") String categoryName,
                                                     @Param("memberId") Long memberId);
    @Query("SELECT t FROM Todo t " +
            "WHERE t.calendar.calendarId = :calendarId " +
            "AND t.isChecked = true ")
    List<Todo> findHomeTodoByCalendarIdAndIsChecked (@Param("calendarId")Long calendarId);

    @Query("SELECT t FROM Todo t " +
            "JOIN FETCH t.member m " +
            "WHERE YEAR(t.createdDate) = :year " +
            "AND MONTH(t.createdDate) = :month " +
            "AND t.week = :week " +
            "AND t.isChecked = true ")
    List<Todo> findTodoBySelectedDateAndIsChecked (@Param("year") Integer year,
                                                @Param("month") Integer month,
                                                @Param("week") Integer week);

    /** for feekback */
    @Query("SELECT t FROM Todo t " +
            "JOIN FETCH t.member m " +
            "WHERE YEAR(t.createdDate) = :year " +
            "AND MONTH(t.createdDate) = :month " +
            "AND t.week = :week " +
            "AND t.isDelay = true ")
    List<Todo> todoForFeedbackByIsDelayTrue (@Param("year") Integer year,
                                             @Param("month") Integer month,
                                             @Param("week") Integer week);

    @Query("SELECT COUNT(t) FROM Todo t " +
            "JOIN t.member m " +
            "WHERE YEAR(t.createdDate) = :year " +
            "AND MONTH(t.createdDate) = :month " +
            "AND t.week = :week " +
            "AND t.priority = :priority")
    Integer countTodoByPriority(@Param("year") Integer year,
                             @Param("month") Integer month,
                             @Param("week") Integer week,
                             @Param("priority") String priority);

    @Query("SELECT COUNT(t) FROM Todo t " +
            "JOIN t.member m " +
            "WHERE YEAR(t.createdDate) = :year " +
            "AND MONTH(t.createdDate) = :month " +
            "AND t.week = :week " +
            "AND t.priority = :priority " +
            "AND t.isChecked = true")
    Integer countTodoByPriorityAndIsChecked(@Param("year") Integer year,
                                         @Param("month") Integer month,
                                         @Param("week") Integer week,
                                         @Param("priority") String priority);

}