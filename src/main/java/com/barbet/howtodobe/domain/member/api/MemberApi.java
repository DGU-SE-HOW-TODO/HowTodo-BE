package com.barbet.howtodobe.domain.member.api;

import com.barbet.howtodobe.domain.member.application.MemberSignInService;
import com.barbet.howtodobe.domain.member.application.MemberSingUpService;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.member.dto.SignInRequestDTO;
import com.barbet.howtodobe.domain.member.dto.SignInResponseDTO;
import com.barbet.howtodobe.domain.member.dto.SignUpRequestDTO;
import com.barbet.howtodobe.global.common.response.Message;
import com.barbet.howtodobe.global.util.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberApi {
    private final MemberSignInService memberSignInService;
    private final TokenProvider tokenProvider;

    private final MemberSingUpService singUpService;

    @PostMapping("/login")
    public ResponseEntity<String> authorize(@RequestBody SignInRequestDTO signInRequestDTO){
        Member _member = memberSignInService.signIn(signInRequestDTO);
        String accessToken = tokenProvider.createToken(_member, 8640000);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken);
        SignInResponseDTO signInResponseDTO = new SignInResponseDTO("정상적으로 로그인 되었습니다.");

        return ResponseEntity.ok().body(accessToken);
        //return new ResponseEntity(signInResponseDTO, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequestDTO request) {
        singUpService.singUp(request);
        return ResponseEntity.ok().build();
    }
}
