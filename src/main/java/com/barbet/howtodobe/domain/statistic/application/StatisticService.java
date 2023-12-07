package com.barbet.howtodobe.domain.statistic.application;

import com.barbet.howtodobe.domain.category.dao.CategoryRepository;
import com.barbet.howtodobe.domain.category.domain.Category;
import com.barbet.howtodobe.domain.member.dao.MemberRepository;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.nowCategory.dao.NowCategoryRepository;
import com.barbet.howtodobe.domain.nowCategory.domain.NowCategory;
import com.barbet.howtodobe.domain.nowFailtag.dao.NowFailtagRepository;
import com.barbet.howtodobe.domain.nowFailtag.domain.NowFailtag;
import com.barbet.howtodobe.domain.statistic.dto.StatisticResponseDTO;
import com.barbet.howtodobe.domain.todo.dao.TodoRepository;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import com.barbet.howtodobe.global.eunse.JwtTokenProvider;
import com.barbet.howtodobe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

import static com.barbet.howtodobe.global.exception.CustomErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class StatisticService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    private final TodoRepository todoRepository;
    private final CategoryRepository categoryRepository;
    private final NowCategoryRepository nowCategoryRepository;
    private final NowFailtagRepository nowFailtagRepository;

    /** 대분류 통계 정보 */
    private List<NowCategory> getWeekCategory(List<Todo> todoList) {
        Map<Category, Long> weekCategoryList = todoList.stream()
                .filter(todo -> todo.getCategory().getCategoryId() != null && todo.getIsChecked())
                .collect(Collectors.groupingBy(Todo::getCategory, Collectors.counting()));

        Integer totalTodoCategoryCnt = todoList.size();

        List<NowCategory> _weekCategory = weekCategoryList.entrySet().stream()
                .map(entry -> {
                    String categoryName = entry.getKey().getName();
                    Integer nowCategoryRate = (totalTodoCategoryCnt != 0)
                            ? (int) (entry.getValue() * 100.0 / totalTodoCategoryCnt)
                            : 0;
                    return NowCategory.builder()
                            .nowCategory(categoryName)
                            .nowCategoryRate(nowCategoryRate)
                            .build();
                })
                .collect(Collectors.toList());

        if (_weekCategory.size() > 3){
            _weekCategory = _weekCategory.subList(0, 3);
        }
        return _weekCategory;
    }

    /** 실패태그 통계 정보 */
    private List<NowFailtag> getWeekFailtagList(List<Todo> todoList) {
        Map<String, Long> weekFailtagList = todoList.stream()
                .filter(todo -> todo.getFailtagName() != null )
                .collect(Collectors.groupingBy(Todo::getFailtagName, Collectors.counting()));

        Integer totalTodoWithFailTagCnt = todoList.size();
        Integer totalFailtagRates = weekFailtagList.values().stream().mapToInt(Long::intValue).sum();

        List<NowFailtag> _weekFailtagList = weekFailtagList.entrySet().stream()
                .map(entry -> {
                    String failtagName = entry.getKey();
                    Integer failtagCount = entry.getValue().intValue();

                    // 실패 태그 비율을 전체 합에 대한 백분율로 계산
                    Integer nowFailtagRate = (totalTodoWithFailTagCnt != 0)
                            ? (int) ((failtagCount * 100.0) / totalFailtagRates)
                            : 0;

                    return NowFailtag.builder()
                            .nowFailtag(failtagName)
                            .nowFailtagRate(nowFailtagRate)
                            .build();
                })
                .collect(Collectors.toList());
        if (_weekFailtagList.size() > 3){
            _weekFailtagList = _weekFailtagList.subList(0, 5);
        }
        return _weekFailtagList;
    }

    /** selectedDate에 따른 통계 값 전체 */
    public StatisticResponseDTO getStatistic (LocalDate selectedDate, HttpServletRequest request) {
        Member member = memberRepository.findByMemberId(jwtTokenProvider.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        
        // 회원인지 체크
        Long memberId = jwtTokenProvider.getUserIdByServlet(request);
        if (memberId != null && !memberId.equals(member.getMemberId())) {
            throw new CustomException(USER_NOT_FOUND);
        }

        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        Integer year = selectedDate.getYear();
        Integer month = selectedDate.getMonthValue();
        Integer week = selectedDate.get(woy);

        /** 투두 관련 */
        // 이번주 투두 리스트
        List<Todo> nowTodoList = todoRepository.findTodoBySelectedDate(year, month, week, memberId);
        List<Todo> nowTodoDoneList = todoRepository.findTodoBySelectedDateAndIsChecked(year, month, week);
        Integer nowTodoCnt = nowTodoList.size();
        Integer nowTodoDoneCnt = nowTodoDoneList.size();

        List<Todo> prevTodoList = todoRepository.findTodoBySelectedDate(year, month, week-1, memberId);
        List<Todo> prveTodoDoneList = todoRepository.findTodoBySelectedDateAndIsChecked(year, month, week-1);
        Integer prevTodoCnt = prevTodoList.size();
        Integer prevTodoDoneCnt = prveTodoDoneList.size();

        Integer rateOfChange;
        if (prevTodoCnt != 0) {
            Double prevTodoRate = (prevTodoCnt == 0) ? 0.0 : ((double) prevTodoDoneCnt / prevTodoCnt) * 100.0;
            Double nowTodoRate = (nowTodoCnt == 0) ? 0.0 : ((double) nowTodoDoneCnt / nowTodoCnt) * 100.0;

            rateOfChange = (prevTodoRate == null || nowTodoRate == null) ? null : nowTodoRate.intValue() - prevTodoRate.intValue();
        } else {
            rateOfChange = 0;
        }

        /** 대분류 통계 */
        List<Todo> todoListForCategory = todoRepository.findTodoBySelectedDate(year, month, week, memberId);
        List<NowCategory> nowCategoryData = getWeekCategory(todoListForCategory);
        String nowBestCateogry = null;
        if (!nowCategoryData.isEmpty()) {
            nowCategoryData = nowCategoryData.stream()
                    .sorted(Comparator.comparing(NowCategory::getNowCategoryRate).reversed())
                    .collect(Collectors.toList());
            nowBestCateogry = nowCategoryData.get(0).getNowCategory();
        }

        /** 실패태그 통계 */
        List<Todo> todoListForFailtag = todoRepository.findTodoBySelectedDate(year, month, week, memberId);
        List<NowFailtag> nowFailTagData = getWeekFailtagList(todoListForFailtag);

        String nowWorstFailTag = null;
        if (!nowFailTagData.isEmpty()) {
            nowFailTagData = nowFailTagData.stream()
                    .sorted(Comparator.comparing(NowFailtag::getNowFailtagRate).reversed())
                    .collect(Collectors.toList());
            nowWorstFailTag = nowFailTagData.get(0).getNowFailtag();
        }

        if (selectedDate.compareTo(LocalDate.parse("2023-12-01")) == 0) {
            List<NowCategory> categoryData = new ArrayList<>();
            categoryData.add(new NowCategory("운동", 50));
            categoryData.add(new NowCategory("공부", 25));
            categoryData.add(new NowCategory("휴식", 25));


            List<NowFailtag> failtagData = new ArrayList<>();
            failtagData.add(new NowFailtag("잠", 40));
            failtagData.add(new NowFailtag("정각병", 25));
            failtagData.add(new NowFailtag("무리한계획", 15));
            failtagData.add(new NowFailtag("슬럼프", 5));
            failtagData.add(new NowFailtag("컨디션난조", 5));

            return new StatisticResponseDTO(LocalDate.parse("2023-12-01"), 5, 3, 8, 6, 15, categoryData, "운동", failtagData, "잠");
        }

        return new StatisticResponseDTO(selectedDate, prevTodoCnt, prevTodoDoneCnt, nowTodoCnt, nowTodoDoneCnt, rateOfChange, nowCategoryData, nowBestCateogry, nowFailTagData, nowWorstFailTag);
    }
}