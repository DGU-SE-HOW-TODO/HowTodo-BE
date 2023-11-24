package com.barbet.howtodobe.domain.statistics.dao;

import com.barbet.howtodobe.domain.statistics.domain.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticsRepository extends JpaRepository<Statistics, Long> {

}
