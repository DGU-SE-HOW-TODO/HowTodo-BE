package com.barbet.howtodobe.domain.statistic.api;

import com.barbet.howtodobe.domain.statistic.application.StatisticService;
import com.barbet.howtodobe.domain.statistic.dto.StatisticResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statistic")
public class StatisticApi {

    private final StatisticService testStatisticService;

    @GetMapping("/{selectedDate}")
    public ResponseEntity<StatisticResponseDTO> getStatistics(
            @PathVariable("selectedDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectedDate,
            HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok().body(testStatisticService.getStatistic(selectedDate, httpServletRequest));
    }
}