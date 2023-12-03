package com.barbet.howtodobe.domain.member.application;

import com.barbet.howtodobe.domain.member.dao.MemberRepository;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.member.dto.SignInRequestDTO;
import com.barbet.howtodobe.domain.member.dto.SignUpRequestDTO;
import com.barbet.howtodobe.domain.member.exception.InvalidPassword;
import com.barbet.howtodobe.global.eunse.JwtTokenProvider;
import com.barbet.howtodobe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.barbet.howtodobe.global.exception.CustomErrorCode.*;

@Service
@RequiredArgsConstructor
public class MemberSignInService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Long signUp(SignUpRequestDTO request) //throws Exception
    {

        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            //throw new Exception("이미 존재하는 이메일입니다.");
            throw new CustomException(EMAIL_ALREADY_EXIST);
        }

        Member member = memberRepository.save(request.toEntity());
        member.encodePassword(passwordEncoder);
        return member.getMemberId();
    }


    public String login(Map<String, String> users) //throws Exception
    {

        Member member = memberRepository.findByEmail(users.get("email"))
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        String password = users.get("password");
        if (!passwordEncoder.matches(password, member.getPassword())) {
            //throw new Exception("잘못된 비밀번호입니다.");
            throw new CustomException(LOGIN_FAILED);
        }

        return jwtTokenProvider.createToken(member.getUsername());
        //jwt 토큰을 생성, 그리고 반환
    }
}
