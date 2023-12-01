package com.barbet.howtodobe.domain.statistic.application;

import com.barbet.howtodobe.domain.category.dao.CategoryRepository;
import com.barbet.howtodobe.domain.failtag.dao.FailtagRepository;
import com.barbet.howtodobe.domain.failtag.domain.Failtag;
import com.barbet.howtodobe.domain.member.dao.MemberRepository;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.nowCategory.dao.NowCategoryRepository;
import com.barbet.howtodobe.domain.nowCategory.domain.NowCategory;
import com.barbet.howtodobe.domain.nowFailtag.dao.NowFailtagRepository;
import com.barbet.howtodobe.domain.nowFailtag.domain.NowFailtag;
import com.barbet.howtodobe.domain.statistic.dto.StatisticResponseDTO;
import com.barbet.howtodobe.domain.todo.dao.TodoRepository;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import com.barbet.howtodobe.global.exception.CustomException;
import com.barbet.howtodobe.global.util.TokenProvider;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

import static com.barbet.howtodobe.global.exception.CustomErrorCode.NOT_EXIST_STATISTICS_INFO;
import static com.barbet.howtodobe.global.exception.CustomErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class StatisticService {
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    private final TodoRepository todoRepository;
    private final CategoryRepository categoryRepository;
    private final NowCategoryRepository nowCategoryRepository;
    private final NowFailtagRepository nowFailtagRepository;


    /** 대분류 통계 정보 */
    // categoryIdList : 한주에 해당하는 대분류 목록
    private List<NowCategory> getWeekCategory(List<Long> categoryIdList, Integer year, Integer month, Integer week) {
        List<NowCategory> nowCategoryDataList = new ArrayList<>();

        for (Long categoryId : categoryIdList) {
            // Optional<Category> category = categoryRepository.findById(categortId);

            // 1. Category id에 해당하는 투두 리스트 가져오기
            List<Todo> todoByCategory = todoRepository.categoryForStatistic(year, month, week, categoryId);

            // TODO 만약 todoByCategory가 null이라면?
            if (!todoByCategory.isEmpty()) {
                // 2. 해당 Category Id에 대한 투두 개수
                Integer categoryTodoCnt = todoByCategory.size();

                // 3. 해당 Category Id에 대한 투두 중, 달성 완료한 개수
                Integer categoryTodoDoneCnt = Math.toIntExact(todoByCategory.stream().filter(Todo::getIsChecked).count());

                String nowCategory = categoryRepository.findCategoryByCategoryId(categoryId).getName();

                Integer nowCategoryRate = categoryTodoCnt > 0 ? Integer.valueOf ((int) (categoryTodoDoneCnt * 100.0) / categoryTodoCnt) : null;

                NowCategory nowCategoryData = new NowCategory(nowCategory, nowCategoryRate);

                nowCategoryDataList.add(nowCategoryData);
            }
        }

        if (nowCategoryDataList != null && !nowCategoryDataList.isEmpty()) {
            // 6. nowCategoryRate에 따라 내림차순 정렬
            nowCategoryDataList.sort(Comparator.comparingInt(NowCategory::getNowCategoryRate).reversed());

            nowCategoryDataList = nowCategoryDataList.stream()
                    .map(category -> new NowCategory(category.getNowCategory(), category.getNowCategoryRate()))
                    .collect(Collectors.toList());
        } else {
            throw new CustomException(NOT_EXIST_STATISTICS_INFO);
        }

        return nowCategoryDataList;
    }

    /** 실패태그 통계 정보 */
    // 매개변수 todoList: selectedDate에 해당하는 투두이면서 FailtagId값이 null인 투두 리스트
    private List<NowFailtag> getWeekFailtagList(List<Todo> todoList) {

        // key : failtagName, value: 각 failtagId별 투두 개수
        Map<String, Long> weekFailtagList = todoList.stream()
                .filter(todo -> todo.getFailtagName() != null)
                .collect(Collectors.groupingBy(Todo::getFailtagName, Collectors.counting()));

        Integer totalTodoWithFailTagCnt = todoList.size();

        return weekFailtagList.entrySet().stream()
                .map(entry -> {
                    String failtagName = entry.getKey();
                    Integer nowFailtagRate = (int) ((entry.getValue() / totalTodoWithFailTagCnt) * 100);

                    return NowFailtag.builder()
                            .nowFailtag(failtagName)
                            .nowFailtagRate(nowFailtagRate)
                            .build();
                })
                .collect(Collectors.toList());
    }

    /** selectedDate에 따른 통계 값 전체 */
    public StatisticResponseDTO getStatistic (Integer year, Integer month, Integer week, HttpServletRequest request) {
//        Member member = memberRepository.findByMemberId(tokenProvider.getMemberId())
//                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        // TODO HttpServletRequest request 추가

        /** 투두 관련 */
        // 이번주 투두 리스트
        List<Todo> nowTodoList = todoRepository.findTodoBySelectedDate(year, month, week);
        List<Todo> nowTodoDoneList = todoRepository.findTodoBySelectedDateAndIsChecked(year, month, week);
        Integer nowTodoCnt = nowTodoList.size();
        Integer nowTodoDoneCnt = nowTodoDoneList.size();

        List<Todo> prevTodoList = todoRepository.findTodoBySelectedDate(year, month, week-1);
        List<Todo> prveTodoDoneList = todoRepository.findTodoBySelectedDateAndIsChecked(year, month, week-1);
        Integer prevTodoCnt = prevTodoList.size();
        Integer prevTodoDoneCnt = prveTodoDoneList.size();

        Integer rateOfChange;
        if (prevTodoCnt != 0) { // 저번주차가 존재하는 경우
            Double prevTodoRate = (prevTodoCnt == 0) ? 0.0 : ((double) prevTodoDoneCnt / prevTodoCnt) * 100.0;
            Double nowTodoRate = (nowTodoCnt == 0) ? 0.0 : ((double) nowTodoDoneCnt / nowTodoCnt) * 100.0;

            rateOfChange = (prevTodoRate == null || nowTodoRate == null) ? null : nowTodoRate.intValue() - prevTodoRate.intValue();
        } else {
            rateOfChange = 0;
        }

        /** 대분류 관련 */
        // 한 주에 해당하는 대분류 목록
        List<Long> nowCategoryIdList = categoryRepository.findCategoryIdsByDate(year, month, week);

        List<NowCategory> nowCategoryData = getWeekCategory(nowCategoryIdList, year, month, week);

        String nowBestCateogry = null;
        if (!nowCategoryData.isEmpty()) {
            NowCategory nowCategory = nowCategoryData.get(0);
            nowBestCateogry = nowCategory.getNowCategory();
        }

        /** 실패 태그 관련 */
        List<Todo> todoList = todoRepository.findTodoBySelectedDate(year, month, week);
        List<NowFailtag> nowFailtagData = getWeekFailtagList(todoList);

        String nowWorstFailTag = null;
        if (!nowFailtagData.isEmpty()) {
            nowFailtagData = nowFailtagRepository.findAllByOrderByNowFailtagRateAsc();
            NowFailtag nowFailtag = nowFailtagData.get(0);
            nowWorstFailTag = nowFailtag.getNowFailtag();
        }

        return new StatisticResponseDTO(prevTodoCnt, prevTodoDoneCnt, nowTodoCnt, nowTodoDoneCnt, rateOfChange, nowCategoryData, nowBestCateogry, nowFailtagData, nowWorstFailTag);
    }
}