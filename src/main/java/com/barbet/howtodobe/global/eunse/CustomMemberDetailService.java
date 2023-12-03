package com.barbet.howtodobe.global.eunse;

import com.barbet.howtodobe.domain.member.dao.MemberRepository;
import com.barbet.howtodobe.global.exception.CustomErrorCode;
import com.barbet.howtodobe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomMemberDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails) memberRepository.findByEmail(username)
                .orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));
    }

}

