package com.barbet.howtodobe.domain.home.application;

import com.barbet.howtodobe.domain.calendar.dao.CalendarRepository;
import com.barbet.howtodobe.domain.category.dao.CategoryRepository;
import com.barbet.howtodobe.domain.category.domain.Category;
import com.barbet.howtodobe.domain.home.dto.HomeResponseDTO;
import com.barbet.howtodobe.domain.member.dao.MemberRepository;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.todo.dao.TodoRepository;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import com.barbet.howtodobe.global.util.JwtTokenProvider;
import com.barbet.howtodobe.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static com.barbet.howtodobe.global.common.exception.CustomResponseCode.*;

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

        Long memberId = jwtTokenProvider.getUserIdByServlet(request);
        if (memberId != null && !memberId.equals(member.getMemberId())) {
            throw new CustomException(USER_NOT_FOUND);
        }

        java.sql.Date sqlDate = java.sql.Date.valueOf(selectedDate);

        Long calendarId = calendarRepository.findBySelectedDate(sqlDate, member.getMemberId())
                .orElseThrow(() -> new CustomException(NOT_EXIST_CALENDAR));

        List<Todo> homeTodoList = todoRepository.findHomeTodoByCalendarId(calendarId);

        Integer homeTodoCnt = homeTodoList.size();
        Integer homeTodoDoneCnt = todoRepository.findHomeTodoByCalendarIdAndIsChecked(calendarId).size();
        Integer rateOfSuccess = calculateCompletionRate(homeTodoCnt, homeTodoDoneCnt);

        List<HomeResponseDTO.TodoCategoryData> todoCategoryDataList = new ArrayList<>();
        Map<Long, List<HomeResponseDTO.TodoCategoryData.TodoData>> todoDataList = new HashMap<>();

        List<Category> AllCategory = categoryRepository.findAllByMemberId(member.getMemberId());
        List<String> allCategoryNameList = new ArrayList<>();

        List<Todo> todayTodoList = new ArrayList<>();
        for (Category category : AllCategory) {
            String categoryName = category.getName();
            allCategoryNameList.add(categoryName);

            List<Todo> todo = todoRepository.findHomeTodoByCalendarIdANDCategoryId(calendarId, categoryName, member.getMemberId());

            if (todo.isEmpty()) {
                todayTodoList.add(null);
            } else {
                todayTodoList.addAll(todo);
            }
        }

        todayTodoList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Todo::getCategory))
                .forEach(((_category, todoList) -> {
                    Stream<Todo> _todoList = todoList.stream()
                            .filter(todo -> Objects.equals(todo.getCategory().getCategoryId(), _category.getCategoryId()));
                    List<HomeResponseDTO.TodoCategoryData.TodoData> tempTodoDataList = _todoList
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
                    System.out.println("temp todo list: ");
                    for (HomeResponseDTO.TodoCategoryData.TodoData t: tempTodoDataList){
                        System.out.println("t: " + t);
                    }
                    todoDataList.put(_category.getCategoryId(), tempTodoDataList);
                }));

        for (String categoryName : allCategoryNameList) {
            Long categoryId = categoryRepository.findByCategoryName(categoryName, member.getMemberId()).orElseThrow(() -> new CustomException(NOT_EXIST_WEEK_CATEGORY));
            HomeResponseDTO.TodoCategoryData todoCategoryData = new HomeResponseDTO.TodoCategoryData(categoryId, categoryName);
            todoCategoryDataList.add(todoCategoryData);
        }
        return new HomeResponseDTO(rateOfSuccess, todoCategoryDataList, todoDataList);
    }
}