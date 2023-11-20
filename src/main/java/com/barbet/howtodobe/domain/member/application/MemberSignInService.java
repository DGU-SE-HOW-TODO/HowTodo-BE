package com.barbet.howtodobe.domain.member.application;

import com.barbet.howtodobe.domain.member.dao.MemberRepository;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.member.dto.SignInRequestDTO;
import com.barbet.howtodobe.domain.member.exception.InvalidPassword;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberSignInService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member signIn(SignInRequestDTO signInRequestDTO){
//        1. email로 멤버 찾기
//        2. password 인코더로 requestDto의 password encoding
//        3. 조회한 멤버의 password와 인코딩한 값을 비교
//        4. 일치하지 않으면 에러 발생
        Member _member = memberRepository.findByEmail(signInRequestDTO.getEmail());
//        _member.setPassword(passwordEncoder.encode(signInRequestDTO.getPassword()));

        if (passwordEncoder.matches(signInRequestDTO.getPassword(), _member.getPassword())) {
            return _member;
        }
        else {
            throw new InvalidPassword();
        }
    }
}
