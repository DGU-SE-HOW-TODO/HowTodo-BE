package com.barbet.howtodobe.domain.category.dao;

import com.barbet.howtodobe.domain.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /** 통계용 */

    // 한 주에 해당하는 대분류 이름 목록 반환 (중복 제거)
    @Query("SELECT DISTINCT c.name FROM Category c " +
            "JOIN FETCH c.member m " +
            "WHERE YEAR(c.createdDate) = :year " +
            "AND MONTH(c.createdDate) = :month " +
            "AND c.week = :week")
    List<String> findCategoryNamesByDate(@Param("year") Integer year,
                                         @Param("month") Integer month,
                                         @Param("week") Integer week);


}
