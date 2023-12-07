package com.barbet.howtodobe.domain.todo.application;

import com.barbet.howtodobe.domain.calendar.dao.CalendarRepository;
import com.barbet.howtodobe.domain.calendar.domain.Calendar;
import com.barbet.howtodobe.domain.member.dao.MemberRepository;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.todo.dao.TodoRepository;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import com.barbet.howtodobe.global.util.JwtTokenProvider;
import com.barbet.howtodobe.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

import static com.barbet.howtodobe.global.common.exception.CustomResponseCode.TODO_NOT_FOUND;
import static com.barbet.howtodobe.global.common.exception.CustomResponseCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TodoFixService {
    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;
    private final CalendarRepository calendarRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public void updateTodoFixed (Long todoId) {
        Member member = memberRepository.findByMemberId(jwtTokenProvider.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new CustomException(TODO_NOT_FOUND));


        System.out.println("todo fixed check:");
        if (!todo.getIsFixed()) {
            System.out.println(todo.getIsFixed());
            List<Calendar> calendars = calendarRepository.findAllByMemberId(member.getMemberId());

            todo.updateTodoFixed(true, member);
            calendars.stream()
                    .forEach(calendar -> {
                        if (todo.getCalendar().getDate().compareTo(calendar.getDate()) >= 0){
                            return;
                        }

                        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
                        LocalDate localDate = LocalDate.parse(calendar.getDate().toString(), formatter);

                        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
                        Integer week = localDate.get(woy);

                            todoRepository.save(Todo.builder()
                            .calendar(calendar)
                            .name(todo.getName())
                            .priority(todo.getPriority())
                            .isFixed(true)
                            .isChecked(false)
                            .isDelay(false)
                            .member(member)
                            .category(todo.getCategory()).week(week)
                            .build());
                    });
        }
        todoRepository.save(todo);
    }
}
