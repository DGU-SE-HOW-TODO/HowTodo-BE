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

    // 대분류 ID로 대분류명 찾기
    Category findCategoryByCategoryId(Long categoryId);

    @Query("SELECT c.categoryId FROM Category c "+
            "WHERE c.name = :categoryName")
    Optional<Long> findByCategoryName(@Param("categoryName") String categoryName);


}
