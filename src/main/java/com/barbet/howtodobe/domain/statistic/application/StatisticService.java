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
    private final FailtagRepository failtagRepository;

    /** 대분류 통계 정보 */
    private List<NowCategory> getWeekCategory(List<Long> categoryIdList, LocalDate selectedDate) {
        List<NowCategory> nowCategoryDataList = new ArrayList<>();

        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        Integer year = selectedDate.getYear();
        Integer month = selectedDate.getMonthValue();
        Integer week = selectedDate.get(woy);

        for (Long categortId : categoryIdList) {
            // Optional<Category> category = categoryRepository.findById(categortId);

            // 1. Category id에 해당하는 투두 리스트 가져오기
            List<Todo> todoByCategory = todoRepository.categoryForStatistic(year, month, week, categortId);

            // 2. 해당 Category Id에 대한 투두 개수
            Integer categoryTodoCnt = todoByCategory.size();

            // 3. 해당 Category Id에 대한 투두 중, 달성 완료한 개수
            Integer categoryTodoDoneCnt = Math.toIntExact(todoByCategory.stream().filter(Todo::getIsChecked).count());

            String nowCategory = categoryRepository.findCategoryNameByCategoryId(categortId);
            Integer nowCategoryRate = categoryTodoCnt > 0 ? Integer.valueOf ((int) (categoryTodoDoneCnt * 100.0) / categoryTodoCnt) : null;

            // 4. NowCategoryDate DTO 객체 생성 후 리스트에 추가
            NowCategory nowCategoryData = new NowCategory(nowCategory, nowCategoryRate);
            
            // 5. 리스트에 최종 출력 값 저장
            nowCategoryDataList.add(nowCategoryData);
        }

        // 6. nowCategoryRate에 따라 내림차순 정렬
        nowCategoryDataList = nowCategoryRepository.findAllByOrderByNowCategoryRateDesc()
                .stream()
                .map(category -> new NowCategory(category.getNowCategory(), category.getNowCategoryRate()))
                .collect(Collectors.toList());

        if (nowCategoryDataList.isEmpty()) {
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
    public StatisticResponseDTO getStatistic (LocalDate selectedDate, HttpServletRequest request) {
//        Member member = memberRepository.findByMemberId(tokenProvider.getMemberId())
//                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        // TODO HttpServletRequest request 추가

        /** 투두 관련 */
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        Integer year = selectedDate.getYear();
        Integer month = selectedDate.getMonthValue();
        Integer week = selectedDate.get(woy);

        // 이번주 투두 리스트
        List<Todo> nowTodoList = todoRepository.findTodoBySelectedDate(year, month, week);
        List<Todo> nowTodoDoneList = todoRepository.findTodoBySelectedDateAndIsChecked(year, month, week);

        // TODO 이번주가 1주차인 경우도 조건 추가
        List<Todo> prevTodoList = todoRepository.findTodoBySelectedDate(year, month, week-1);
        List<Todo> prveTodoDoneList = todoRepository.findTodoBySelectedDateAndIsChecked(year, month, week-1);

        Integer prevTodoCnt = prevTodoList.size();
        Integer prevTodoDoneCnt = prveTodoDoneList.size();
        Integer nowTodoCnt = nowTodoList.size();
        Integer nowTodoDoneCnt = nowTodoDoneList.size();

        // TODO 이번주나 지난주에 한일이 없거나 아예 투두가 없는 경우 로직 추가
        // +) rateOfChange가 음수인 경우엔 달성률이 더 떨어진건데 이건 프론트가 알아서 처리?
        Double prevTodoRate = (prevTodoCnt == 0) ? 0.0 : ((double) prevTodoDoneCnt / prevTodoCnt) * 100.0;
        Double nowTodoRate = (nowTodoCnt == 0) ? 0.0 : ((double) nowTodoDoneCnt / nowTodoCnt) * 100.0;

        Integer rateOfChange = (prevTodoRate == null || nowTodoRate == null) ? null : nowTodoRate.intValue() - prevTodoRate.intValue();

        /** 대분류 관련 */
        List<Long> nowCategoryIdList = categoryRepository.findCategoryIdsByDate(year, month, week);
        List<NowCategory> nowCategoryData = getWeekCategory(nowCategoryIdList, selectedDate);

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