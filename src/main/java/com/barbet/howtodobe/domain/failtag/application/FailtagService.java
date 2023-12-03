package com.barbet.howtodobe.domain.failtag.application;

import com.barbet.howtodobe.domain.failtag.dao.FailtagRepository;
import com.barbet.howtodobe.domain.failtag.domain.AllFailtag;
import com.barbet.howtodobe.domain.failtag.domain.Failtag;
import com.barbet.howtodobe.domain.failtag.dto.FailtagRequestDTO;
import com.barbet.howtodobe.domain.member.dao.MemberRepository;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.todo.dao.TodoRepository;
import com.barbet.howtodobe.global.eunse.JwtTokenProvider;
import com.barbet.howtodobe.global.exception.CustomErrorCode;
import com.barbet.howtodobe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

import static com.barbet.howtodobe.global.exception.CustomErrorCode.FAILTAG_COUNT_IS_NOT_FIVE;
import static com.barbet.howtodobe.global.exception.CustomErrorCode.USER_NOT_FOUND;

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

        List<Failtag> failtagList = failtagRepository.findFailtagsBySelectedDate(week);

        if (failtagList.size() == 5) {
            throw new CustomException(FAILTAG_COUNT_IS_NOT_FIVE);
        }

        List<String> selectedFailtagList = request.getSelectedFailtagList();

        if (selectedFailtagList.size() != 5) { // 실패태그 선택 5개해야 함
            throw new CustomException(FAILTAG_COUNT_IS_NOT_FIVE);
        }

        for (String failtagName : selectedFailtagList) {
            String selectedDate = request.getSelectedDate();
            LocalDate date = LocalDate.parse(
                    selectedDate, DateTimeFormatter.ISO_DATE);
            Integer _woy = date.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
            String[] datechunks = selectedDate.split("-");
            int _year= Integer.parseInt(datechunks[0]);
            int _month = Integer.parseInt(datechunks[1]);
            int _week = _woy;
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
