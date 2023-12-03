package com.barbet.howtodobe.domain.feedback.api;

import com.barbet.howtodobe.domain.feedback.application.FeedbackService;
import com.barbet.howtodobe.domain.feedback.dto.FeedbackResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feedback")
public class FeedbackApi {
    private final FeedbackService feedbackService;

    @GetMapping("/{selectedDate}")
    public ResponseEntity<FeedbackResponseDTO> getFeedback(
            @PathVariable("selectedDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectedDate,
            HttpServletRequest httpServletRequest) {
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        Integer year = selectedDate.getYear();
        Integer month = selectedDate.getMonthValue();
        Integer week = selectedDate.get(woy);
        return ResponseEntity.ok().body(feedbackService.getFeedback(year, month, week, httpServletRequest));
    }

//    @GetMapping("/{selectedDate}")
//    public ResponseEntity<TestFeedbackResponseDTO> getFeedback(
//            @PathVariable("selectedDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectedDate,
//            HttpServletRequest httpServletRequest) {
//        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
//        Integer year = selectedDate.getYear();
//        Integer month = selectedDate.getMonthValue();
//        Integer week = selectedDate.get(woy);
//        return ResponseEntity.ok().body(testFeedbackService._getFeedback(year, month, week, httpServletRequest));
//    }
}