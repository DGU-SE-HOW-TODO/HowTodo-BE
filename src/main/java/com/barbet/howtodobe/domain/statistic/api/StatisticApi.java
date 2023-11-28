package com.barbet.howtodobe.domain.statistic.api;

import com.barbet.howtodobe.domain.statistic.application.StatisticService;
import com.barbet.howtodobe.domain.statistic.dto.StatisticResponseDTO;
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
@RequestMapping("/statistic")
public class StatisticApi {

    private final StatisticService statisticService;
    @GetMapping("/{selectedDate}")
    public ResponseEntity<StatisticResponseDTO> getStatistics (@PathVariable("selectedDate")LocalDate selectedDate,
                                                               HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok().body(statisticService.getStatistic(selectedDate, httpServletRequest));
    }
}