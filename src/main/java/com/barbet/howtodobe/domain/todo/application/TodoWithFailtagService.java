package com.barbet.howtodobe.domain.todo.application;

import com.barbet.howtodobe.domain.category.dao.CategoryRepository;
import com.barbet.howtodobe.domain.failtag.dao.FailtagRepository;
import com.barbet.howtodobe.domain.member.dao.MemberRepository;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.todo.dao.TodoRepository;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import com.barbet.howtodobe.global.exception.CustomException;
import com.barbet.howtodobe.global.util.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

import static com.barbet.howtodobe.global.exception.CustomErrorCode.*;

@Service
@RequiredArgsConstructor
public class TodoWithFailtagService {

    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final TodoRepository todoRepository;
    private final FailtagRepository failtagRepository;

    public void enrollTodoWithFailtag (Long todoId, Long failTagId) {
        Member member = memberRepository.findByMemberId(tokenProvider.getMemberId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new CustomException(TODO_NOT_FOUND));

        LocalDate selectedDate = todo.getCreatedDate();

        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        Integer year = selectedDate.getYear();
        Integer month = selectedDate.getMonthValue();
        Integer week = selectedDate.get(woy);

        List<String> thisWeekFailtagList = failtagRepository.findFailtagsBySelectedDate(year, month, week);
        String selectedFailTag = failtagRepository.findNameByFailtagId(failTagId);

        // 일단 이번주 실패태그(5개) 잘 등록 되어 있는지 체크
        if (thisWeekFailtagList != null && !thisWeekFailtagList.isEmpty()) {
            // thisWeekFailtagList에 selectedFailTag 있는지 확인
            if (!thisWeekFailtagList.contains(selectedFailTag)) {
                throw new CustomException(INVALID_FAILTAG);
            } else {
                todo.updateTodoWithFailtag(failTagId);
            }
        } else {
            throw new CustomException(NOT_EXIST_WEEK_FAILTAG);
        }
    }
}
