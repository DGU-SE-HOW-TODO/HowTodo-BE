package com.barbet.howtodobe.domain.feedback.application;

import com.barbet.howtodobe.domain.category.dao.CategoryRepository;
import com.barbet.howtodobe.domain.feedback.dto.FeekbackResponseDTO;
import com.barbet.howtodobe.domain.feedback.message.FeedbackDetailMessage;
import com.barbet.howtodobe.domain.feedback.message.FeedbackMessage;
import com.barbet.howtodobe.domain.feedback.message.RandomDelayTips;
import com.barbet.howtodobe.domain.member.dao.MemberRepository;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.nowCategory.dao.NowCategoryRepository;
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
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static com.barbet.howtodobe.global.exception.CustomErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class FeekbackService {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    private final TodoRepository todoRepository;
    private final CategoryRepository categoryRepository;
    private final NowCategoryRepository nowCategoryRepository;

    /** 미루기 피드백 */
    public FeekbackResponseDTO getFeedback (LocalDate selectedDate, HttpServletRequest request) {
        Member member = memberRepository.findByMemberId(tokenProvider.getMemberId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        Integer year = selectedDate.getYear();
        Integer month = selectedDate.getMonthValue();
        Integer week = selectedDate.get(woy);

        /**
         * 1. 날짜와 isDelayed값이 true인 투두 가져오고, size확인
         * 2. 해당 리스트에서 자주 미룬 대분류명 추출하기
         * 3. size에 따라 보여주는 메시지값 설정하기
         */

        List<Todo> isDelayTodoList = todoRepository.todoForFeedbackByIsDelayTrue(year, month, week);

        Integer delayTodoCnt = isDelayTodoList.size();
        String mostDelayCategory = categoryRepository.findCategoryNameById(getMostDelayCategoryId(isDelayTodoList));

        // delayTodoCnt에 따라 보여주는 메시지 값 설정하기
        String delayMessage = getMessageByDelayCnt(delayTodoCnt, mostDelayCategory);
        String delayDetailMessage = getDetailMessageByDelayCnt(delayTodoCnt);

        return new FeekbackResponseDTO();
    }

    // 가장 많이 미룬 카테고리 id
    private Long getMostDelayCategoryId(List<Todo> isDelayTodoList) {
        
        // key: 카테고리 id, value: 미룬 투두 수
        Map<Long, Long> categoryCntMap = isDelayTodoList.stream()
                .collect(Collectors.groupingBy(todo -> todo.getCategory().getCategoryId(), Collectors.counting()));

        Long mostDelayCategoryId = categoryCntMap.entrySet().stream()
                .max(Comparator.comparingLong(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(null);

        return mostDelayCategoryId;
    }

    // delay Message
    private String getMessageByDelayCnt(Integer delayTodoCnt, String mostDelayCategory) {
        if (delayTodoCnt == 0) {
            // 미룬거 없으면 반환할 메시지 없음
            return null;
        } else {
            return FeedbackMessage.DELAY_MESSAGE.format(delayTodoCnt, mostDelayCategory);
        }
    }

    // delay Detail Message
    private String getDetailMessageByDelayCnt(Integer delayTodoCnt) {
        RandomDelayTips[] randomDelayTips = RandomDelayTips.values();
        int randomIdx = (int) (Math.random() * randomDelayTips.length);
        String randomDelayTipMessage = randomDelayTips[randomIdx].getMessage();

        if (delayTodoCnt == 0) {
            return null; // 미룬거 없으면 반환할 메시지 없음
        } else if (delayTodoCnt >= 1 && delayTodoCnt <= 5) {
            // 1~5개 미뤘을 때 메시지
            return FeedbackDetailMessage.DELAY_1_5.format(delayTodoCnt);
        } else {
            // 6개 이상 미뤘으면 랜덤으로 tip message 반환
            return FeedbackDetailMessage.DELAY_6.format(randomDelayTipMessage);
        }
    }
}
