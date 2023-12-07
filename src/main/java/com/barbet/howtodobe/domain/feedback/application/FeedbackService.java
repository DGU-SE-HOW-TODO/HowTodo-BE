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
import com.barbet.howtodobe.global.util.JwtTokenProvider;
import com.barbet.howtodobe.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.barbet.howtodobe.global.common.exception.CustomResponseCode.USER_NOT_FOUND;

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

    /** 미루기 피드백
     * 가장 많이 미룬 카테고리 id
     */
    private Long getMostDelayCategoryId(List<Todo> isDelayTodoList) {
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
            return null;
        } else if (delayTodoCnt >= 1 && delayTodoCnt <= 5) {
            return FeedbackDetailMessage.DELAY_DETAIL_MESSAGE_1_5.format(delayTodoCnt);
        } else {
            return FeedbackDetailMessage.DELAY_DETAIL_MESSAGE_6.format(randomDelayTipMessage);
        }
    }


    /** 피드백 조회 */
    public FeedbackResponseDTO getFeedback (Integer year, Integer month, Integer week, HttpServletRequest request) {
        Member member = memberRepository.findByMemberId(jwtTokenProvider.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Long memberId = jwtTokenProvider.getUserIdByServlet(request);
        if (memberId != null && !memberId.equals(member.getMemberId())) {
            throw new CustomException(USER_NOT_FOUND);
        }


        /** 달성률 피드백 */
        // 이번주 투두 관련
        List<Todo> nowTodoList = todoRepository.findTodoBySelectedDate(year, month, week, memberId);
        Integer nowTodoCnt = nowTodoList.size(); 
        List<Todo> nowTodoDoneList = todoRepository.findTodoBySelectedDateAndIsChecked(year, month, week);
        Integer nowTodoDoneCnt = nowTodoDoneList.size();
        
        // 저번주 투두 관련
        List<Todo> prevTodoList = todoRepository.findTodoBySelectedDate(year, month, week, memberId);
        Integer prevTodoCnt = prevTodoList.size();
        List<Todo> prevTodoDoneList = todoRepository.findTodoBySelectedDateAndIsChecked(year, month, week);
        Integer prevTodoDoneCnt = prevTodoDoneList.size();

        Double prevTodoRate = (prevTodoCnt == 0) ? 0.0 : ((double) prevTodoDoneCnt / prevTodoCnt) * 100.0;
        Double nowTodoRate = (nowTodoCnt == 0) ? 0.0 : ((double) nowTodoDoneCnt / nowTodoCnt) * 100.0;

        Integer rateOfChange = calculateRateOfChange(prevTodoRate, nowTodoRate);

        String rateMessage;
        String rateDetailMessage;

        if (nowTodoRate >= 50) {
            rateMessage = null;
            rateDetailMessage = null;
        } else {
            if (rateOfChange > 0) {
                rateMessage = FeedbackMessage.INCREASED_RATE_MESSAGE.format(rateOfChange);
            } else {
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

        delayMessage = getMessageByDelayCnt(delayTodoCnt, mostDelayCategory);
        delayDetailMessage = getDetailMessageByDelayCnt(delayTodoCnt);

        return new FeedbackResponseDTO(
                month,
                week,
                rateMessage,
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
