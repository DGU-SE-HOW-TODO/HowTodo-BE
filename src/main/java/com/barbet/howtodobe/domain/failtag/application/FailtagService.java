package com.barbet.howtodobe.domain.failtag.application;

import com.barbet.howtodobe.domain.failtag.dao.FailtagRepository;
import com.barbet.howtodobe.domain.failtag.domain.Failtag;
import com.barbet.howtodobe.domain.failtag.dto.FailtagRequestDTO;
import com.barbet.howtodobe.domain.member.dao.MemberRepository;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.global.util.JwtTokenProvider;
import com.barbet.howtodobe.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

import static com.barbet.howtodobe.global.common.exception.CustomResponseCode.FAILTAG_COUNT_IS_NOT_FIVE;
import static com.barbet.howtodobe.global.common.exception.CustomResponseCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class FailtagService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final FailtagRepository failtagRepository;

    public void select5Failtags (FailtagRequestDTO request) {
        Member member = memberRepository.findByMemberId(jwtTokenProvider.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
        LocalDate localDate = LocalDate.parse(request.getSelectedDate(), formatter);
        
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        Integer week = localDate.get(woy);
        
        // 해당 주의 실패태그 가져오기
         List<Failtag> failtagList = failtagRepository.findFailtagsBySelectedDateAndMember(week, member.getMemberId());
         
        if (failtagList.size() > 5) {
            throw new CustomException(FAILTAG_COUNT_IS_NOT_FIVE);
        }

        List<String> selectedFailtagList = request.getSelectedFailtagList();

        if (selectedFailtagList.size() != 5) { // 실패 태그는 최대 5개까지 가능
            throw new CustomException(FAILTAG_COUNT_IS_NOT_FIVE);
        }

        for (String failtagName : selectedFailtagList) {
            String selectedDate = request.getSelectedDate();
            LocalDate date = LocalDate.parse(
                    selectedDate, DateTimeFormatter.ISO_DATE);
            TemporalField _woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
            Integer _week = localDate.get(_woy);

            String[] datechunks = selectedDate.split("-");
            int _year= Integer.parseInt(datechunks[0]);
            int _month = Integer.parseInt(datechunks[1]);

            Failtag failtag = Failtag.builder()
                    .member(member)
                    .year(_year)
                    .month(_month)
                    .week(_week)
                    .name(failtagName)
                    .selectedFailtagList(List.of(failtagName))
                    .build();

            failtagRepository.save(failtag);
        }
    }
}
