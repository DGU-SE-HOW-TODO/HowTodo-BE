package com.barbet.howtodobe.domain.nowCategory.dao;

import com.barbet.howtodobe.domain.nowCategory.domain.NowCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NowCategoryRepository extends JpaRepository<NowCategory, Long> {

    // nowCategoryRate 내림차순 정렬
    List<NowCategory> findAllByOrderByNowCategoryRateDesc();
}
