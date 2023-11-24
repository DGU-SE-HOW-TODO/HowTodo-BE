package com.barbet.howtodobe.domain.statistics.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statistic")
public class StatisticsApi {
    // 캘린더에서 날짜를 선택하면 (년, 월, 일) 해당 날짜에 맞는 API 넘겨줌
}
