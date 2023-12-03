package com.barbet.howtodobe.domain.feedback.application;

import com.barbet.howtodobe.domain.category.dao.CategoryRepository;
import com.barbet.howtodobe.domain.feedback.dto.FeedbackResponseDTO;
import com.barbet.howtodobe.domain.feedback.message.FeedbackDetailMessage;
import com.barbet.howtodobe.domain.feedback.message.FeedbackMessage;
import com.barbet.howtodobe.domain.feedback.message.RandomDelayTips;
import com.barbet.howtodobe.domain.member.dao.MemberRepository;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.todo.dao.TodoRepository;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import com.barbet.howtodobe.global.eunse.JwtTokenProvider;
import com.barbet.howtodobe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.barbet.howtodobe.global.exception.CustomErrorCode.*;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private final TodoRepository todoRepository;
    private final CategoryRepository categoryRepository;

    private Integer calculateCompletionRate(Integer todoCount, Integer todoDoneCount) {
        if (todoCount == null || todoCount == 0) {
            return 0;
        }

        double completionRate = ((double) todoDoneCount / todoCount) * 100.0;
        return (int) completionRate;
    }
    private Integer calculateRateOfChange(Double prevRate, Double nowRate) {
        return (prevRate == null || nowRate == null) ? null : nowRate.intValue() - prevRate.intValue();
    }

    /** 미루기 피드백 */
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
            return FeedbackDetailMessage.DELAY_DETAIL_MESSAGE_1_5.format(delayTodoCnt);
        } else {
            // 6개 이상 미뤘으면 랜덤으로 tip message 반환
            return FeedbackDetailMessage.DELAY_DETAIL_MESSAGE_6.format(randomDelayTipMessage);
        }
    }

    /** 피드백 조회 */
    public FeedbackResponseDTO getFeedback (Integer year, Integer month, Integer week, HttpServletRequest request) {
        Member member = memberRepository.findByMemberId(jwtTokenProvider.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        // 회원인지 체크
        Long memberId = jwtTokenProvider.getUserIdByServlet(request);
        if (memberId != null && !memberId.equals(member.getMemberId())) {
            throw new CustomException(USER_NOT_FOUND);
        }

        /** 달성률 피드백 */
        List<Todo> nowTodoList = todoRepository.findTodoBySelectedDate(year, month, week);
        Integer nowTodoCnt = nowTodoList.size(); // 이번주 전체 투두
        List<Todo> nowTodoDoneList = todoRepository.findTodoBySelectedDateAndIsChecked(year, month, week);
        Integer nowTodoDoneCnt = nowTodoDoneList.size(); // 이번주 달성한 투두
        List<Todo> prevTodoList = todoRepository.findTodoBySelectedDate(year, month, week-1);
        Integer prevTodoCnt = prevTodoList.size(); // 저번주 전체 투두
        List<Todo> prevTodoDoneList = todoRepository.findTodoBySelectedDateAndIsChecked(year, month, week);
        Integer prevTodoDoneCnt = prevTodoDoneList.size(); // 저번주 달성한 투두

        Double prevTodoRate = (prevTodoCnt == 0) ? 0.0 : ((double) prevTodoDoneCnt / prevTodoCnt) * 100.0;
        Double nowTodoRate = (nowTodoCnt == 0) ? 0.0 : ((double) nowTodoDoneCnt / nowTodoCnt) * 100.0;

        Integer rateOfChange = calculateRateOfChange(prevTodoRate, nowTodoRate);

        String rateMessage;
        String rateDetailMessage;

        if (nowTodoRate >= 50) { // 달성률이 50% 이상인 경우
            rateMessage = null;
            rateDetailMessage = null;
        } else { // 달성률이 50% 미만인 경우
            if (rateOfChange > 0) { // 달성률이 오른 경우
                rateMessage = FeedbackMessage.INCREASED_RATE_MESSAGE.format(rateOfChange);
            } else { // 달성률이 떨어진 경우
                rateMessage = FeedbackMessage.DECREASED_RATE_MESSAGE.format(rateOfChange);
            }
            rateDetailMessage = FeedbackDetailMessage.RATE_DETAIL_MESSAGE.getDetailMessage();
        }

        /** 우선순위 피드백 */
        String priorityMessage;
        String priorityDetailMessage;

        Integer veryImportantTodoCnt = todoRepository.countTodoByPriority(year, month, week, "매우 중요");
        Integer veryImportantTodoDoneCnt = todoRepository.countTodoByPriorityAndIsChecked(year, month, week, "매우 중요");
        Integer importantTodoCnt = todoRepository.countTodoByPriority(year, month, week, "중요");
        Integer importantTodoDoneCnt = todoRepository.countTodoByPriorityAndIsChecked(year, month, week, "중요");
        Integer notImportantTodoCnt = todoRepository.countTodoByPriority(year, month, week, "중요하지 않음");
        Integer notImportantTodoDoneCnt = todoRepository.countTodoByPriorityAndIsChecked(year, month, week, "중요하지 않음");

        Integer veryImportantTodoPercent = calculateCompletionRate(veryImportantTodoCnt, veryImportantTodoDoneCnt);
        Integer importantTodoPercent = calculateCompletionRate(importantTodoCnt, importantTodoDoneCnt);
        Integer notImportantTodoPercent = calculateCompletionRate(notImportantTodoCnt, notImportantTodoDoneCnt);

        // 우선순위가 [매우 중요]인게 달성률이 가장 낮은 경우
        if (veryImportantTodoPercent < importantTodoPercent && veryImportantTodoPercent < notImportantTodoPercent) {
            priorityMessage = FeedbackMessage.PRIORITY_MESSAGE.getMessage();
            priorityDetailMessage = FeedbackDetailMessage.PRIORITY_DETAIL_MESSAGE.getDetailMessage();
        } else {
            priorityMessage = null;
            priorityDetailMessage = null;
        }

        /** 미루기 피드백 */
        List<Todo> isDelayTodoList = todoRepository.todoForFeedbackByIsDelayTrue(year, month, week);

        Integer delayTodoCnt = isDelayTodoList.size();

        String delayMessage = null;
        String delayDetailMessage = null;
        String mostDelayCategory;
        if (categoryRepository.findCategoryByCategoryId(getMostDelayCategoryId(isDelayTodoList)) == null) {
            mostDelayCategory = null;
        } else {
            mostDelayCategory = categoryRepository.findCategoryByCategoryId(getMostDelayCategoryId(isDelayTodoList)).getName();
        }

        // delayTodoCnt에 따라 보여주는 메시지 값 설정하기
        delayMessage = getMessageByDelayCnt(delayTodoCnt, mostDelayCategory);
        delayDetailMessage = getDetailMessageByDelayCnt(delayTodoCnt);

        return new FeedbackResponseDTO(rateMessage,
                rateDetailMessage,
                veryImportantTodoPercent,
                importantTodoPercent,
                notImportantTodoPercent,
                priorityMessage,
                priorityDetailMessage,
                delayMessage,
                delayDetailMessage);
    }
}
