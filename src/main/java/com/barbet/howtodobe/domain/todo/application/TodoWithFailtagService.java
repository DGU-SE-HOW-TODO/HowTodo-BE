package com.barbet.howtodobe.domain.todo.application;

import com.barbet.howtodobe.domain.calendar.dao.CalendarRepository;
import com.barbet.howtodobe.domain.calendar.domain.Calendar;
import com.barbet.howtodobe.domain.category.dao.CategoryRepository;
import com.barbet.howtodobe.domain.failtag.dao.FailtagRepository;
import com.barbet.howtodobe.domain.failtag.domain.Failtag;
import com.barbet.howtodobe.domain.member.dao.MemberRepository;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.todo.dao.TodoRepository;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import com.barbet.howtodobe.domain.todo.dto.TodoFailtagRequestDTO;
import com.barbet.howtodobe.global.util.JwtTokenProvider;
import com.barbet.howtodobe.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.barbet.howtodobe.global.common.exception.CustomResponseCode.*;

@Service
@RequiredArgsConstructor
public class TodoWithFailtagService {

    private final CategoryRepository categoryRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final TodoRepository todoRepository;
    private final FailtagRepository failtagRepository;

    private final CalendarRepository calendarRepository;
    public List<String> findFailtagsBySelectedDate(Integer year, Integer month, Integer week, HttpServletRequest request) {
        Member member = memberRepository.findByMemberId(jwtTokenProvider.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Long memberId = jwtTokenProvider.getUserIdByServlet(request);
        if (memberId != null && !memberId.equals(member.getMemberId())) {
            throw new CustomException(USER_NOT_FOUND);
        }

        List<Failtag> failtags = failtagRepository.findFailtagsBySelectedDate(week);

        List<String> selectedFailtagList = failtags.stream()
                .flatMap(failtag -> failtag.getSelectedFailtagList().stream())
                .collect(Collectors.toList());
        if (selectedFailtagList.size() > 5) {
            selectedFailtagList = selectedFailtagList.subList(selectedFailtagList.size() - 5, selectedFailtagList.size());
        }

        return selectedFailtagList;
    }

    public Boolean enrollTodoWithFailtag (Long todoId, TodoFailtagRequestDTO request, HttpServletRequest httpServletRequest) {
        Member member = memberRepository.findByMemberId(jwtTokenProvider.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Long memberId = jwtTokenProvider.getUserIdByServlet(httpServletRequest);
        if (memberId != null && !memberId.equals(member.getMemberId())) {
            throw new CustomException(USER_NOT_FOUND);
        }

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new CustomException(TODO_NOT_FOUND));

        Integer week = todo.getWeek();

        List<Failtag> failtags = failtagRepository.findFailtagsBySelectedDate(week);
        List<String> selectedFailtagList = failtags.stream()
                .flatMap(failtag -> failtag.getSelectedFailtagList().stream())
                .collect(Collectors.toList());


        if (selectedFailtagList != null && !selectedFailtagList.isEmpty()) {
            if (!selectedFailtagList.contains(request.getFailTagName())) {
                throw new CustomException(INVALID_FAILTAG);
            } else {
                todo.updateTodoWithFailtag(request.getFailTagName(), request.getIsDelay(),false);
                todoRepository.save(todo);
            }
        } else {
            throw new CustomException(NOT_EXIST_WEEK_FAILTAG);
        }

        if (request.getIsDelay()) {
            todo.updateTodoDelay(true, member);
            todoRepository.save(todo);

            Long calendarId = calendarRepository.findBySelectedDate(
                            Date.valueOf(todo.getCalendar().getDate().toLocalDate().plusDays(1)), memberId)
                    .orElseThrow(() -> new CustomException(NOT_EXIST_CALENDAR));

            Calendar calendar = calendarRepository.findById(calendarId)
                    .orElseThrow(() -> new CustomException(NOT_EXIST_CALENDAR));
            ;

            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
            LocalDate localDate = LocalDate.parse(calendar.getDate().toString(), formatter);

            TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
            Integer _week = localDate.get(woy);

            Todo tomorrowTodo = Todo.builder()
                    .calendar(calendar)
                    .name(todo.getName())
                    .priority(todo.getPriority())
                    .isFixed(todo.getIsFixed())
                    .isChecked(false)
                    .isDelay(false)
                    .member(member)
                    .category(todo.getCategory())
                    .week(_week)
                    .build();

            todoRepository.save(tomorrowTodo);
        }
        return request.getIsDelay();
    }
}