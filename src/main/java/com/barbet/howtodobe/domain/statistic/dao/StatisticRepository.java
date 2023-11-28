package com.barbet.howtodobe.domain.statistic.dao;

import com.barbet.howtodobe.domain.statistic.domain.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticRepository extends JpaRepository<Statistic, Long> {

}
