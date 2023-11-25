package com.barbet.howtodobe.domain.todo.dao;

import com.barbet.howtodobe.domain.calendar.domain.Calendar;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
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

}
