package com.barbet.howtodobe.domain.failtag.application;

import com.barbet.howtodobe.domain.failtag.dao.FailtagRepository;
import com.barbet.howtodobe.domain.failtag.domain.AllFailtag;
import com.barbet.howtodobe.domain.failtag.domain.Failtag;
import com.barbet.howtodobe.domain.failtag.dto.FailtagRequestDTO;
import com.barbet.howtodobe.domain.member.dao.MemberRepository;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.todo.dao.TodoRepository;
import com.barbet.howtodobe.global.exception.CustomException;
import com.barbet.howtodobe.global.util.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.barbet.howtodobe.global.exception.CustomErrorCode.FAILTAG_COUNT_IS_NOT_FIVE;
import static com.barbet.howtodobe.global.exception.CustomErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class FailtagService {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final FailtagRepository failtagRepository;

    public void select5Failtags (FailtagRequestDTO request) {
        Member member = memberRepository.findByMemberId(tokenProvider.getMemberId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        List<String> selectedFailtagList = request.getSelectedFailtagList();

        if (selectedFailtagList.size() != 5) { // 실패태그 선택 5개해야 함
            throw new CustomException(FAILTAG_COUNT_IS_NOT_FIVE);
        }

        for (String failtagName : selectedFailtagList) {
            Failtag failtag = Failtag.builder()
                    .member(member)
                    .year(request.getYear())
                    .month(request.getMonth())
                    .week(request.getWeek())
                    .selectedFailtagList(List.of(failtagName))
                    .build();
            failtagRepository.save(failtag);
        }
    }
}
