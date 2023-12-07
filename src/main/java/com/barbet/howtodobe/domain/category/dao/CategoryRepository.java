package com.barbet.howtodobe.domain.category.dao;

import com.barbet.howtodobe.domain.category.domain.Category;
import com.barbet.howtodobe.domain.failtag.domain.Failtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findCategoryByCategoryId(Long categoryId);

    @Query("SELECT c.categoryId FROM Category c "+
            "WHERE c.name = :categoryName AND c.member.memberId = :memberId")
    Optional<Long> findByCategoryName(@Param("categoryName") String categoryName,
                                      @Param("memberId") Long memberId);

    @Query("SELECT c FROM Category c " +
            "WHERE c.member.memberId = :memberId")
    List<Category> findAllByMemberId(@Param("memberId") Long memberId);


}
