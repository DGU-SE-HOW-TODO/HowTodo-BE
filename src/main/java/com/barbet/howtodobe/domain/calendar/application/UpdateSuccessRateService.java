package com.barbet.howtodobe.domain.calendar.application;

import com.barbet.howtodobe.domain.calendar.dao.CalendarRepository;
import com.barbet.howtodobe.domain.todo.dao.TodoRepository;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdateSuccessRateService {
    private final CalendarRepository calendarRepository;
    private final TodoRepository todoRepository;

    public int updateSuccessRate(Long calendarId) {
        System.out.println("update success");
        int totalCnt = todoRepository.getTotalTodoCnt(calendarId);
        int successedCnt = todoRepository.getSuccessedTodoCnt(calendarId);

        int updatedRate = calendarRepository.updateSuccessRate(
                successedCnt / totalCnt);

        return updatedRate;
    }
}
