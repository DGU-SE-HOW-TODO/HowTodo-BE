package com.barbet.howtodobe.domain.todo.application;

import com.barbet.howtodobe.domain.category.dao.CategoryRepository;
import com.barbet.howtodobe.domain.failtag.dao.FailtagRepository;
import com.barbet.howtodobe.domain.failtag.domain.Failtag;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final TodoRepository todoRepository;
    private final FailtagRepository failtagRepository;

    private final TodoDelayService todoDelayService;

    public List<String> findFailtagsBySelectedDate(Integer year, Integer month, Integer week, HttpServletRequest request) {
        Member member = memberRepository.findByMemberId(jwtTokenProvider.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        // 회원인지 체크
        Long memberId = jwtTokenProvider.getUserIdByServlet(request);
        if (memberId != null && !memberId.equals(member.getMemberId())) {
            throw new CustomException(USER_NOT_FOUND);
        }

        List<Failtag> failtags = failtagRepository.findFailtagsBySelectedDate(week);

        // 각 Failtag 객체에서 selectedFailtagList를 추출하여 리스트로 만들기
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

        // 회원인지 체크
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


        // 일단 이번주 실패태그(5개) 잘 등록 되어 있는지 체크
        if (selectedFailtagList != null && !selectedFailtagList.isEmpty()) {
            // thisWeekFailtagList에 selectedFailTag 있는지 확인
            if (!selectedFailtagList.contains(request.getFailTagName())) {
                throw new CustomException(INVALID_FAILTAG);
            } else {
                todo.updateTodoWithFailtag(request.getFailTagName(), request.getIsDelay(),false);
                todoRepository.save(todo);
            }
        } else {
            throw new CustomException(NOT_EXIST_WEEK_FAILTAG);
        }

        todoDelayService.updateTodoDelayed(memberId, request, httpServletRequest);

        // 미뤘는지 여부 반환
        return request.getIsDelay();
    }


}