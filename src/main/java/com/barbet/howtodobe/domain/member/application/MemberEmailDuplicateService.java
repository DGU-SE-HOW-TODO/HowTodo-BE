package com.barbet.howtodobe.domain.member.application;

import com.barbet.howtodobe.domain.member.dao.MemberRepository;
import com.barbet.howtodobe.domain.member.domain.Member;
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
        Member _member = memberRepository.findByEmail(email);
        if (_member.getMemberId() < 0){
            return false;
        }
        return true;
    }

}
