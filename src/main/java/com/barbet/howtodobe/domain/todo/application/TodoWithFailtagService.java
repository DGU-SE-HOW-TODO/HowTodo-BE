package com.barbet.howtodobe.domain.todo.application;

import com.barbet.howtodobe.domain.category.dao.CategoryRepository;
import com.barbet.howtodobe.domain.failtag.dao.FailtagRepository;
import com.barbet.howtodobe.domain.failtag.domain.Failtag;
import com.barbet.howtodobe.domain.member.dao.MemberRepository;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.todo.dao.TodoRepository;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import com.barbet.howtodobe.domain.todo.dto.TodoFailtagRequestDTO;
import com.barbet.howtodobe.global.exception.CustomException;
import com.barbet.howtodobe.global.util.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.barbet.howtodobe.global.exception.CustomErrorCode.*;

@Service
@RequiredArgsConstructor
public class TodoWithFailtagService {

    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final TodoRepository todoRepository;
    private final FailtagRepository failtagRepository;

    public List<String> findFailtagsBySelectedDate(Integer year, Integer month, Integer week) {
        List<Failtag> failtags = failtagRepository.findFailtagsBySelectedDate(week);

        // 각 Failtag 객체에서 selectedFailtagList를 추출하여 리스트로 만들기
        List<String> selectedFailtagList = failtags.stream()
                .flatMap(failtag -> failtag.getSelectedFailtagList().stream())
                .collect(Collectors.toList());
        if (selectedFailtagList.size() > 5) {
            selectedFailtagList = selectedFailtagList.subList(selectedFailtagList.size() - 5, selectedFailtagList.size());
        } else {
            selectedFailtagList = selectedFailtagList;
        }

        return selectedFailtagList;
    }

    public Boolean enrollTodoWithFailtag (Long todoId, TodoFailtagRequestDTO request) {
//        Member member = memberRepository.findByMemberId(tokenProvider.getMemberId())
//                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        // TODO 임시 멤버
        Member tempMember = memberRepository.findByEmail("senuej37@gmail.com");

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new CustomException(TODO_NOT_FOUND));

        LocalDate selectedDate = todo.getCreatedDate();

        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        Integer year = selectedDate.getYear();
        Integer month = selectedDate.getMonthValue();
        Integer week = selectedDate.get(woy);

        List<String> thisWeekFailtagList = findFailtagsBySelectedDate(year, month, week);

        // 일단 이번주 실패태그(5개) 잘 등록 되어 있는지 체크
        if (thisWeekFailtagList != null && !thisWeekFailtagList.isEmpty()) {
            // thisWeekFailtagList에 selectedFailTag 있는지 확인
            if (!thisWeekFailtagList.contains(request.getFailTagName())) {
                throw new CustomException(INVALID_FAILTAG);
            } else {
                todo.updateTodoWithFailtag(request.getFailTagName(), request.getIsDelay(),false);
                todoRepository.save(todo);
            }
        } else {
            throw new CustomException(NOT_EXIST_WEEK_FAILTAG);
        }

        // 미뤘는지 여부 반환
        return request.getIsDelay();
    }
}
