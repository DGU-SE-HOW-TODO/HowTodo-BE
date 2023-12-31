package com.barbet.howtodobe.domain.member.api;

import com.barbet.howtodobe.domain.member.application.MemberEmailDuplicateService;
import com.barbet.howtodobe.domain.member.application.MemberSignInService;
import com.barbet.howtodobe.domain.member.dto.SignInResponseDTO;
import com.barbet.howtodobe.domain.member.dto.SignUpRequestDTO;
import com.barbet.howtodobe.global.common.exception.CustomResponseCode;
import com.barbet.howtodobe.global.common.response.ApiStatus;
import com.barbet.howtodobe.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static com.barbet.howtodobe.global.common.exception.CustomResponseCode.EMAIL_ALREADY_EXIST;

@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberApi {
    private final MemberSignInService memberSignInService;
    private final MemberEmailDuplicateService memberEmailDuplicateService;

    @PostMapping("/signup")
    public ResponseEntity<Void> join(@Valid @RequestBody SignUpRequestDTO request) {
        memberSignInService.signUp(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<SignInResponseDTO> login(@RequestBody Map<String, String> user) {
        String token = memberSignInService.login(user);
        SignInResponseDTO signInResponseDTO = new SignInResponseDTO("정상적으로 로그인 되었습니다.", token);
        return new ResponseEntity(signInResponseDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/{email}", produces = "application/json")
    public ResponseEntity<ApiStatus> emailDuplicate(@PathVariable("email") String email){
        HttpHeaders httpHeaders = new HttpHeaders();

        if (memberEmailDuplicateService.EmailDuplicateCheck(email)) {
            throw  new CustomException(EMAIL_ALREADY_EXIST);
        }

        try {
            boolean isDuplicate = memberEmailDuplicateService.EmailDuplicateCheck(email);
            if (isDuplicate) {
                return new ResponseEntity(
                        new ApiStatus(CustomResponseCode.DUPLICATE_EMAIL, "중복된 이메일"),
                        httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity(
                    new ApiStatus(CustomResponseCode.SUCCESS, "이메일 중복 X"),
                    httpHeaders, HttpStatus.OK);
        }
        catch (RuntimeException e){
            return new ResponseEntity(
                    new ApiStatus(CustomResponseCode.INTERNAL_SERVER_ERROR, e.getMessage()),
                    httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
