package com.barbet.howtodobe.domain.todo.application;

import com.barbet.howtodobe.domain.calendar.dao.CalendarRepository;
import com.barbet.howtodobe.domain.calendar.domain.Calendar;
import com.barbet.howtodobe.domain.category.dao.CategoryRepository;
import com.barbet.howtodobe.domain.category.domain.Category;
import com.barbet.howtodobe.domain.member.dao.MemberRepository;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.todo.dao.TodoRepository;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import com.barbet.howtodobe.domain.todo.dto.TodoAssignRequestDTO;
import com.barbet.howtodobe.global.util.JwtTokenProvider;
import com.barbet.howtodobe.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;

import static com.barbet.howtodobe.global.common.exception.CustomResponseCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TodoAssignService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final TodoRepository todoRepository;
    private final CategoryRepository categoryRepository;

    private final CalendarRepository calendarRepository;

    public Todo assignTodo(TodoAssignRequestDTO todoAssignRequestDTO) {
        Member member = memberRepository.findByMemberId(jwtTokenProvider.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Date date = Date.valueOf(todoAssignRequestDTO.getSelectedDate());

        Calendar cal = calendarRepository.findByDate(date, member.getMemberId()).orElseThrow(() -> new RuntimeException("날짜에 맞는 캘린더가 존재하지 않습니다."));
        Category category = categoryRepository.findById(todoAssignRequestDTO.getTodoCategoryId()).orElseThrow(() -> new RuntimeException("카테고리가 존재하지 않습니다."));

        Todo todo = Todo.builder()
                .calendar(cal)
                .category(category)
                .member(member)
                .isFixed(false)
                .isDelay(false)
                .isChecked(false)
                .week(cal.calculateWeek()+1)
                .name(todoAssignRequestDTO.getTodo())
                .priority(todoAssignRequestDTO.getPriority())
                .build();

        Todo _todo = todoRepository.save(todo);
        System.out.println("todo:" + _todo);

        if (_todo.getTodoId() > 0) {
            return _todo;
        } else {
            throw new RuntimeException("투두 등록 실패: _todo.getTodoId()가 0보다 작거나 같습니다.");
        }
    }

}