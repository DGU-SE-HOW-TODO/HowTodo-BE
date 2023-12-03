package com.barbet.howtodobe.domain.home.application;

import com.barbet.howtodobe.domain.calendar.application.UpdateSuccessRateService;
import com.barbet.howtodobe.domain.category.dao.CategoryRepository;
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
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.barbet.howtodobe.global.exception.CustomErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final TodoRepository todoRepository;

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

        LocalDate todayDate = LocalDate.now(); // 현재 날짜

        // 선택한 날짜에 대한 투두 리스트 불러옴
        List<Todo> homeTodoList = todoRepository.findHomeTodoBySelectedDate(selectedDate);

        Integer homeTodoCnt = homeTodoList.size();
        Integer homeTodoDoneCnt = todoRepository.findHomeTodoBySelectedDateAndIsChecked(selectedDate).size();
        Integer rateOfSuccess = calculateCompletionRate(homeTodoCnt, homeTodoDoneCnt);

        List<HomeResponseDTO.TodoCategoryData> todoCategoryDataList = new ArrayList<>();
        List<HomeResponseDTO.TodoCategoryData.TodoData> todoDataList = new ArrayList<>();

        // 투두 카테고리 별 grouping
        homeTodoList.stream()
                .collect(Collectors.groupingBy(Todo::getCategory))
                .forEach(((category, todoList) -> {
                    Long categoryId = category.getCategoryId();
                    String todoCategory = category.getName();

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

                    // todoCategoryData -> [todoCategoryId, <todoData>]
                    HomeResponseDTO.TodoCategoryData todocategoryData = new HomeResponseDTO.TodoCategoryData(categoryId, todoDataList, todoCategory);

                    todoCategoryDataList.add(todocategoryData);
                    todoDataList.addAll(tempTodoDataList);
                }));

        return new HomeResponseDTO(rateOfSuccess, todoCategoryDataList, todoDataList);
    }
}