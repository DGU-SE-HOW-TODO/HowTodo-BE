package com.barbet.howtodobe.domain.home.application;

import com.barbet.howtodobe.domain.calendar.dao.CalendarRepository;
import com.barbet.howtodobe.domain.category.dao.CategoryRepository;
import com.barbet.howtodobe.domain.category.domain.Category;
import com.barbet.howtodobe.domain.home.dto.HomeResponseDTO;
import com.barbet.howtodobe.domain.member.dao.MemberRepository;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.todo.dao.TodoRepository;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import com.barbet.howtodobe.global.eunse.JwtTokenProvider;
import com.barbet.howtodobe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static com.barbet.howtodobe.global.exception.CustomErrorCode.*;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final TodoRepository todoRepository;
    private final CalendarRepository calendarRepository;
    private final CategoryRepository categoryRepository;

    private Integer calculateCompletionRate(Integer todoCount, Integer todoDoneCount) {
        if (todoCount == null || todoCount == 0) {
            return 0;
        }

        double completionRate = ((double) todoDoneCount / todoCount) * 100.0;
        return (int) completionRate;
    }

    public HomeResponseDTO getHomeInfo (LocalDate selectedDate, HttpServletRequest request) {
        Member member = memberRepository.findByMemberId(jwtTokenProvider.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        // 회원인지 체크
        Long memberId = jwtTokenProvider.getUserIdByServlet(request);
        if (memberId != null && !memberId.equals(member.getMemberId())) {
            throw new CustomException(USER_NOT_FOUND);
        }

        LocalDate todayDate = LocalDate.now(); // 현재 날짜

        // LocalDate를 Date로 변환
        java.sql.Date sqlDate = java.sql.Date.valueOf(selectedDate);

        Long calendarId = calendarRepository.findBySelectedDate(sqlDate, member.getMemberId())
                .orElseThrow(() -> new CustomException(NOT_EXIST_CALENDAR));

        // 선택한 날짜에 대한 투두 리스트 불러옴
        List<Todo> homeTodoList = todoRepository.findHomeTodoByCalendarId(calendarId);

        Integer homeTodoCnt = homeTodoList.size();
        Integer homeTodoDoneCnt = todoRepository.findHomeTodoBySelectedDateAndIsChecked(selectedDate).size();
        Integer rateOfSuccess = calculateCompletionRate(homeTodoCnt, homeTodoDoneCnt);

        List<HomeResponseDTO.TodoCategoryData> todoCategoryDataList = new ArrayList<>();
        List<HomeResponseDTO.TodoCategoryData.TodoData> todoDataList = new ArrayList<>();

        /*
        todo를 통해서 카테고리를 불러오면, todo가 없는 경우에 카테고리가 안뜸
        그래서 카테고리는 어차피 전부 고정이니까 카테고리를 먼저 싹 다 불러오고
        불러온 카테고리 명칭 + 선택한 날짜에 맞는 todo를 뿌려주면 됨.
         */

        List<Category> AllCategory = categoryRepository.findAll();
        List<String> allCategoryNameList = new ArrayList<>();

        List<Todo> todayTodoList = new ArrayList<>();
        for (Category category : AllCategory) {
            String categoryName = category.getName();
            allCategoryNameList.add(categoryName); // 카테고리 리스트 만듦

            List<Todo> todo = todoRepository.findHomeTodoByCalendarIdANDCategoryId(calendarId, categoryName, member.getMemberId());

            if (todo.isEmpty()) {
                todayTodoList.add(null);
            } else {
                todayTodoList.addAll(todo);
            }
        }

        todayTodoList.stream()
                .filter(Objects::nonNull) // Null 필터링 추가
                .collect(Collectors.groupingBy(Todo::getCategory))
                .forEach(((category, todoList) -> {
                    // 할 일 목록을 DTO로 변환
                    List<HomeResponseDTO.TodoCategoryData.TodoData> tempTodoDataList = todoList.stream()
                            .map(todo -> new HomeResponseDTO.TodoCategoryData.TodoData(
                                    todo.getTodoId(),
                                    todo.getName(),
                                    todo.getPriority(),
                                    todo.getIsChecked(),
                                    todo.getIsFixed(),
                                    todo.getIsDelay(),
                                    todo.getFailtagName()
                            ))
                            .collect(Collectors.toList());
                    todoDataList.addAll(tempTodoDataList);
                }));

        // 카테고리 이름들을 TodoCategoryData에 추가
        for (String categoryName : allCategoryNameList) {
            Long categoryId = categoryRepository.findByCategoryName(categoryName, member.getMemberId()).orElseThrow(() -> new CustomException(NOT_EXIST_WEEK_CATEGORY));
            HomeResponseDTO.TodoCategoryData todoCategoryData = new HomeResponseDTO.TodoCategoryData(categoryId, todoDataList, categoryName);
            todoCategoryDataList.add(todoCategoryData);
        }
        return new HomeResponseDTO(rateOfSuccess, todoCategoryDataList, todoDataList);
    }
}