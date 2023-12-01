package com.barbet.howtodobe.domain.statistic.dao;

import com.barbet.howtodobe.domain.category.domain.Category;
import com.barbet.howtodobe.domain.statistic.domain.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Long> {
}
