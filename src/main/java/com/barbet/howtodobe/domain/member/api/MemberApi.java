package com.barbet.howtodobe.domain.member.api;

import com.barbet.howtodobe.domain.member.application.MemberSignInService;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.member.dto.SignInRequestDTO;
import com.barbet.howtodobe.domain.member.dto.SignInResponseDTO;
import com.barbet.howtodobe.global.common.response.ApiStatus;
import com.barbet.howtodobe.global.common.response.HowTodoStatus;
import com.barbet.howtodobe.global.common.response.Message;
import com.barbet.howtodobe.global.util.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberApi {
    private final MemberSignInService memberSignInService;
    private final TokenProvider tokenProvider;

    @PostMapping(value = "/member/login", produces = "application/json")
    public ResponseEntity<ApiStatus> authorize(@RequestBody SignInRequestDTO signInRequestDTO){
        Member _member = memberSignInService.signIn(signInRequestDTO);
        String accessToken = tokenProvider.createToken(_member, 8640000);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken);
        SignInResponseDTO signInResponseDTO = new SignInResponseDTO("정상적으로 로그인 되었습니다.");

        return new ResponseEntity(Message
                .builder()
                .apiStatus(new ApiStatus(HowTodoStatus.OK, "로그인 성공"))
                .data(signInResponseDTO).build(),
                httpHeaders, HttpStatus.OK);
    }
}
