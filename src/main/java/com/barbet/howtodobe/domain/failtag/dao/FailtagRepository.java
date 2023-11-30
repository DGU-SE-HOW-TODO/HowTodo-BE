package com.barbet.howtodobe.domain.failtag.dao;

import com.barbet.howtodobe.domain.failtag.domain.Failtag;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FailtagRepository extends JpaRepository<Failtag, Long> {

    @Query("SELECT f.selectedFailtagList FROM Failtag f " +
            "JOIN FETCH f.member m " +
            "WHERE f.year = :year " +
            "AND f.month = :month " +
            "AND f.week = :week")
    List<String> findFailtagsBySelectedDate (@Param("year") Integer year,
                                             @Param("month") Integer month,
                                             @Param("week") Integer week);

    @Query("SELECT f.name FROM Failtag f WHERE f.failtagId = :failtagId")
    String findNameByFailtagId(@Param("failtagId") Long failtagId);



}
