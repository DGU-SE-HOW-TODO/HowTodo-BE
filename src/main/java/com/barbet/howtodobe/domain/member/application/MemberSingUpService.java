package com.barbet.howtodobe.domain.member.application;

import com.barbet.howtodobe.domain.member.dao.MemberRepository;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.member.dto.SignUpRequestDTO;
import com.barbet.howtodobe.global.exception.CustomException;
import com.barbet.howtodobe.global.util.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.barbet.howtodobe.global.exception.CustomErrorCode.EMAIL_ALREADY_EXIST;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberSingUpService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final MemberEmailDuplicateService memberEmailDuplicateService;

    public Long singUp (SignUpRequestDTO request) {
        if (!memberEmailDuplicateService.EmailDuplicateCheck(request.getEmail())) {
            throw  new CustomException(EMAIL_ALREADY_EXIST);
        }

        Member member = memberRepository.save(request.toEntity());
        member.encodePassword(passwordEncoder);

        return member.getMemberId();
    }
}
