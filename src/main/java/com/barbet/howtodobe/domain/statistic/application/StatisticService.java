package com.barbet.howtodobe.domain.statistic.application;

import com.barbet.howtodobe.domain.category.dao.CategoryRepository;
import com.barbet.howtodobe.domain.category.domain.Category;
import com.barbet.howtodobe.domain.member.dao.MemberRepository;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.nowCategory.dao.NowCategoryRepository;
import com.barbet.howtodobe.domain.nowCategory.domain.NowCategory;
import com.barbet.howtodobe.domain.statistic.dto.test_StatisticResponseDTO;
import com.barbet.howtodobe.domain.todo.dao.TodoRepository;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import com.barbet.howtodobe.global.exception.CustomException;
import com.barbet.howtodobe.global.util.TokenProvider;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

import static com.barbet.howtodobe.global.exception.CustomErrorCode.USER_NOT_FOUND;

@Data
@Service
@RequiredArgsConstructor
public class StatisticService {
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    private final TodoRepository todoRepository;
    private final CategoryRepository categoryRepository;
    private final NowCategoryRepository nowCategoryRepository;

    /** 대분류 통계 정보 */
    public List<NowCategory> getWeekCategory(List<Long> categoryIdList, LocalDate selectedDate) {
        List<NowCategory> nowCategoryDataList = new ArrayList<>();
        String nowBestCateogry = null;

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
            Integer categoryTodoDoneCnt = Math.toIntExact(todoByCategory.stream().filter(Todo::isChecked).count());

            String nowCategory = categoryRepository.findCategoryNameById(categortId);
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

        // TODO 7. nowBestCategory -> 나중에 한번에 반환할 때 작성
//        if (!nowCategoryDataList.isEmpty()) {
//            NowCategory nowCategory = nowCategoryDataList.get(0);
//            nowBestCateogry = nowCategory.getNowCategory();
//        }

        return nowCategoryDataList;
    }
}