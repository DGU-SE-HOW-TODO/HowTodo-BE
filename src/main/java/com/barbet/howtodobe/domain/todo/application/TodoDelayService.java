package com.barbet.howtodobe.domain.todo.application;

import com.barbet.howtodobe.domain.calendar.dao.CalendarRepository;
import com.barbet.howtodobe.domain.calendar.domain.Calendar;
import com.barbet.howtodobe.domain.member.dao.MemberRepository;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.todo.dao.TodoRepository;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import com.barbet.howtodobe.domain.todo.dto.TodoFailtagRequestDTO;
import com.barbet.howtodobe.global.eunse.JwtTokenProvider;
import com.barbet.howtodobe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

import static com.barbet.howtodobe.global.exception.CustomErrorCode.TODO_NOT_FOUND;
import static com.barbet.howtodobe.global.exception.CustomErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TodoDelayService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final TodoRepository todoRepository;

    private final CalendarRepository calendarRepository;


    public void updateTodoDelayed(Long todoId, TodoFailtagRequestDTO request, HttpServletRequest httpServletRequest){

        Member member = memberRepository.findByMemberId(jwtTokenProvider.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        Long memberId = jwtTokenProvider.getUserIdByServlet(httpServletRequest);
        if (memberId != null && !memberId.equals(member.getMemberId())) {
            throw new CustomException(USER_NOT_FOUND);
        }

        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new CustomException(TODO_NOT_FOUND));

        todo.updateTodoDelay(true, member);
        todoRepository.save(todo);
        List<Calendar> calendars = calendarRepository.findAllByMemberId(member.getMemberId());

        Calendar calendar = calendars.stream()
                .filter(cal -> todo.getCalendar().getDate().compareTo(cal.getDate()) >= 0)
                .findFirst().orElseThrow(() -> new CustomException(TODO_NOT_FOUND));

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
        LocalDate localDate = LocalDate.parse(calendar.getDate().toString(), formatter);

        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        Integer week = localDate.get(woy);

        Todo tomorrowTodo = Todo.builder()
                .calendar(calendar)
                .name(todo.getName())
                .priority(todo.getPriority())
                .isFixed(todo.getIsFixed())
                .isChecked(false)
                .isDelay(true)
                .member(member)
                .category(todo.getCategory())
                .week(week)
                .build();

        todoRepository.save(tomorrowTodo);
    }

}

