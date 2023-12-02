package com.barbet.howtodobe.domain.failtag.dao;

import com.barbet.howtodobe.domain.failtag.domain.Failtag;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FailtagRepository extends JpaRepository<Failtag, Long> {

    @Query("SELECT f FROM Failtag f "+
            "WHERE f.week = :week")
    List<Failtag> findFailtagsBySelectedDate (@Param("week") Integer week);

    @Transactional
    @Modifying
    @Query("DELETE FROM Failtag f " +
            "WHERE f.member.memberId = :memberId OR f.member.memberId IS NULL")
    int deleteFailTagByMemberId(@Param("memberId") Long memberId);

}
