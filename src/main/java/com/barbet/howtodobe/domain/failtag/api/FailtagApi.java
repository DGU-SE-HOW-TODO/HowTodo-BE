package com.barbet.howtodobe.domain.failtag.api;

import com.barbet.howtodobe.domain.failtag.application.FailtagService;
import com.barbet.howtodobe.domain.failtag.dto.FailtagRequestDTO;
import com.barbet.howtodobe.domain.feedback.dto.FeedbackResponseDTO;
import com.barbet.howtodobe.domain.todo.application.TodoWithFailtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/failtag")
public class FailtagApi {

    private final FailtagService failtagService;
    private final TodoWithFailtagService todoWithFailtagService;

    @PostMapping("/todoCategory")
    public ResponseEntity<Void> select5Failtag(@RequestBody FailtagRequestDTO request) {
        failtagService.select5Failtags(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{selectedDate}")
    public ResponseEntity<List<String>> getSelected5Failtag(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectedDate) {
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        Integer year = selectedDate.getYear();
        Integer month = selectedDate.getMonthValue();
        Integer week = selectedDate.get(woy);
        return ResponseEntity.ok().body(todoWithFailtagService.findFailtagsBySelectedDate(year, month, week));
    }
}
