package com.barbet.howtodobe.domain.home.api;

import com.barbet.howtodobe.domain.home.application.HomeService;
import com.barbet.howtodobe.domain.home.dto.HomeResponseDTO;
import com.barbet.howtodobe.domain.statistic.application.StatisticService;
import com.barbet.howtodobe.domain.statistic.dto.StatisticResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeApi {

    private final HomeService homeService;
    @ResponseBody
    @GetMapping(value = "/{selectedDate}", produces = "application/json")
    public ResponseEntity<HomeResponseDTO> getHomeInfo(
            @PathVariable(value = "selectedDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectedDate,
            HttpServletRequest httpServletRequest) {

        if (selectedDate == null) {
            // selectedDate를 선택하지 않은 경우 현재 날짜로 설정
            selectedDate = LocalDate.now();
        }
        return ResponseEntity.ok().body(homeService.getHomeInfo(selectedDate, httpServletRequest));
    }
}
