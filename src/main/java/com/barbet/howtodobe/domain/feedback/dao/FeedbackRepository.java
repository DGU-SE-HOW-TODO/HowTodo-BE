package com.barbet.howtodobe.domain.feedback.dao;

import com.barbet.howtodobe.domain.feedback.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
