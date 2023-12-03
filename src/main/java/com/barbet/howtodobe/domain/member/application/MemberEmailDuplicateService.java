package com.barbet.howtodobe.domain.member.application;

import com.barbet.howtodobe.domain.member.dao.MemberRepository;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.global.common.response.ApiStatus;
import com.barbet.howtodobe.global.exception.CustomErrorCode;
import com.barbet.howtodobe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberEmailDuplicateService {
    private final MemberRepository memberRepository;

    @Transactional
    public boolean EmailDuplicateCheck(String email){
        Member _member = memberRepository.findByEmailForDuplicateCheck(email);

        if (_member == null){
            return false;
        } else if (_member.getMemberId() > 0) {
            return true;
        }
        else{
            throw new RuntimeException("유효하지 않은 멤버 조회");
        }
    }

}
