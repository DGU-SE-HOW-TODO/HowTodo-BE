package com.barbet.howtodobe.domain.member.api;

import com.barbet.howtodobe.domain.member.application.MemberEmailDuplicateService;
import com.barbet.howtodobe.domain.member.application.MemberSignInService;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.member.dto.EmailDuplicateRequstDTO;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberApi {
    private final MemberSignInService memberSignInService;
    private final TokenProvider tokenProvider;

    private final MemberEmailDuplicateService memberEmailDuplicateService;

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

    @GetMapping(value = "/member/{email}", produces = "application/json")
    public ResponseEntity<ApiStatus> emailDuplicate(@PathVariable("email") String email){
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            boolean isDuplicate = memberEmailDuplicateService.EmailDuplicateCheck(email);
            if (isDuplicate) {
                return new ResponseEntity(
                        new ApiStatus(HowTodoStatus.DUPLICATE_EMAIL, "중복된 이메일"),
                        httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity(
                    new ApiStatus(HowTodoStatus.OK, "이메일 중복 X"),
                    httpHeaders, HttpStatus.OK);
        }
        catch (RuntimeException e){
            return new ResponseEntity(
                    new ApiStatus(HowTodoStatus.INTERNEL_SERVER_ERROR, e.getMessage()),
                    httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
