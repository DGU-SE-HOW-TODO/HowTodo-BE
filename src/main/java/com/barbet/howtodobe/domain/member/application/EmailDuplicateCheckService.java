package com.barbet.howtodobe.domain.member.application;

import com.barbet.howtodobe.domain.member.dao.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailDuplicateCheckService {
    private final MemberRepository memberRepository;


}
