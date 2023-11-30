package com.barbet.howtodobe.domain.nowFailtag.dao;

import com.barbet.howtodobe.domain.category.domain.Category;
import com.barbet.howtodobe.domain.nowCategory.domain.NowCategory;
import com.barbet.howtodobe.domain.nowFailtag.domain.NowFailtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NowFailtagRepository extends JpaRepository<NowFailtag, Long> {

    // nowFailtagRate 오름차순 정렬
    List<NowFailtag> findAllByOrderByNowFailtagRateAsc();
}
