package com.barbet.howtodobe.domain.home.application;

import com.barbet.howtodobe.domain.calendar.application.UpdateSuccessRateService;
import com.barbet.howtodobe.domain.category.dao.CategoryRepository;
import com.barbet.howtodobe.domain.home.dto.HomeResponseDTO;
import com.barbet.howtodobe.domain.member.dao.MemberRepository;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.todo.dao.TodoRepository;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import com.barbet.howtodobe.global.exception.CustomException;
import com.barbet.howtodobe.global.util.TokenProvider;
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

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final TodoRepository todoRepository;
    private final CategoryRepository categoryRepository;

    // TODO 실패태그 관련 로직 추가


    private Integer calculateCompletionRate(Integer todoCount, Integer todoDoneCount) {
        if (todoCount == null || todoCount == 0) {
            return 0;
        }

        double completionRate = ((double) todoDoneCount / todoCount) * 100.0;
        return (int) completionRate;
    }

    public HomeResponseDTO getHomeInfo (LocalDate selectedDate, HttpServletRequest request) {
        Member member = memberRepository.findByMemberId(tokenProvider.getMemberId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        LocalDate todayDate = LocalDate.now();

        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        Integer year = selectedDate.getYear();
        Integer month = selectedDate.getMonthValue();
        Integer week = selectedDate.get(woy);

        List<Todo> homeTodoList = todoRepository.findTodoBySelectedDate(year, month, week);

        Integer homeTodoCnt = homeTodoList.size();
        Integer homeTodoDoneCnt = todoRepository.findTodoBySelectedDateAndIsChecked(year, month, week).size();
        Integer rateOfSuccess = calculateCompletionRate(homeTodoCnt, homeTodoDoneCnt);

        List<HomeResponseDTO.TodoCategoryData> todoCategoryDataList = new ArrayList<>();
        List<HomeResponseDTO.TodoCategoryData.TodoData> todoDataList = new ArrayList<>();

        // 투두 카테고리 별 grouping
        homeTodoList.stream()
                .collect(Collectors.groupingBy(Todo::getCategory))
                .forEach(((category, todoList) -> {
                    Long categoryId = category.getCategoryId();

                    List<HomeResponseDTO.TodoCategoryData.TodoData> tempTodoDataList = todoList.stream()
                            .map(todo -> new HomeResponseDTO.TodoCategoryData.TodoData(
                                    todo.getTodoId(),
                                    todo.getCategory().getName(),
                                    todo.getName(),
                                    todo.getPriority(),
                                    todo.isChecked(),
                                    todo.isFixed(),
                                    todo.getFailtag()
                            ))
                            .collect(Collectors.toList());

                    HomeResponseDTO.TodoCategoryData categoryData = new HomeResponseDTO.TodoCategoryData(categoryId, todoDataList);
                    todoCategoryDataList.add(categoryData);
                    todoDataList.addAll(tempTodoDataList);
                });
        return new HomeResponseDTO(rateOfSuccess, todoCategoryDataList, todayDate, selectedDate, todoDataList);
    }
}
