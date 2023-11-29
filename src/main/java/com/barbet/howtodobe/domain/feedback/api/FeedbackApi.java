package com.barbet.howtodobe.domain.feedback.api;

import com.barbet.howtodobe.domain.feedback.application.FeedbackService;
import com.barbet.howtodobe.domain.feedback.dto.FeedbackResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feedback")
public class FeedbackApi {
    private final FeedbackService feedbackService;

    @GetMapping("/{selectedDate")
    public ResponseEntity<FeedbackResponseDTO> getFeedback(
            @PathVariable("selectedDate") LocalDate selectedDate,
            HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok().body(feedbackService.getFeedback(selectedDate, httpServletRequest));
    }
}