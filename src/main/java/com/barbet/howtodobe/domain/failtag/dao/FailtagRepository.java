package com.barbet.howtodobe.domain.failtag.dao;

import com.barbet.howtodobe.domain.failtag.domain.Failtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FailtagRepository extends JpaRepository<Failtag, Long> {

}
