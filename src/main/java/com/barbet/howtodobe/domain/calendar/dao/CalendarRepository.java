package com.barbet.howtodobe.domain.calendar.dao;

import com.barbet.howtodobe.domain.calendar.domain.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    @Transactional
    @Modifying
    @Query(value = "UPDATE Calendar c " +
            "SET c.successRate = :updated_rate")
    void updateSuccessRate(@Param("updated_rate") int updated_rate);


    @Query(value = "SELECT c FROM Calendar c " +
            "WHERE c.date = :date_val AND c.member.memberId = :memberId")
    Optional<Calendar> findByDate(@Param("date_val") Date date,
                                  @Param("memberId") Long memberId);

    @Query("SELECT c.calendarId FROM Calendar c " +
            "WHERE c.date = :sqlDate AND c.member.memberId = :memberId")
    Optional<Long> findBySelectedDate(@Param("sqlDate") Date sqlDate, @Param("memberId") Long memberId);

}
