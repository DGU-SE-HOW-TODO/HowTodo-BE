package com.barbet.howtodobe.domain.member.application;

import com.barbet.howtodobe.domain.member.dao.MemberRepository;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.member.dto.SignUpRequestDTO;
import com.barbet.howtodobe.global.util.JwtTokenProvider;
import com.barbet.howtodobe.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

import static com.barbet.howtodobe.global.common.exception.CustomResponseCode.*;

@Service
@RequiredArgsConstructor
public class MemberSignInService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Long signUp(SignUpRequestDTO request)
    {

        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new CustomException(EMAIL_ALREADY_EXIST);
        }

        Member member = memberRepository.save(request.toEntity());
        member.encodePassword(passwordEncoder);
        return member.getMemberId();
    }


    public String login(Map<String, String> users)
    {

        Member member = memberRepository.findByEmail(users.get("email"))
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        String password = users.get("password");
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(LOGIN_FAILED);
        }

        return jwtTokenProvider.createToken(member.getUsername());
    }
}
