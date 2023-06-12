package showMoim.api.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import showMoim.api.common.ApiResponse;
import showMoim.api.member.dto.MemberJoinDto.RegisterForm;
import showMoim.api.member.service.MemberEmailService;
import showMoim.api.member.service.MemberService;

/**
 * 회원가입 관련 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/member/join", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberJoinController {

    private final MemberService memberService;
    private final MemberEmailService memberEmailService;

    /**
     * 회원가입 인증 메일 요청 API
     */
    @PostMapping("/email/verify/send")
    public ApiResponse<?> sendVerificationEmail(@RequestBody RegisterForm form) {
        // 회원가입 시작 -> 회원 DB 생성
        memberService.join(RegisterForm.builder()
            .email(form.getEmail())
            .password("임시비번")
            .passwordConfirm("임시비번")
            .nickname("임시닉넴")
            .build());

        // 인증 메일 전송
        memberEmailService.sendVerificationEmailCode(form.getEmail());

        // 응답
        return ApiResponse.of(ApiResponse.SUCCESS_STATUS, "인증메일 발송 성공", null);
    }

    /**
     * 회원가입 인증메일 코드 검증 API
     */
    @PostMapping("/email/verify")
    public ApiResponse<?> verifyEmail(@RequestBody RegisterForm form) {
        // 인증 메일 검증
        Boolean isVerified = memberEmailService.verifyEmail(form.getEmail(), form.getCode());

        if (!isVerified) {
            return ApiResponse.of(ApiResponse.ERROR_STATUS, "이메일 인증 실패", null);
        }

        // 응답
        return ApiResponse.of(ApiResponse.SUCCESS_STATUS, "이메일 인증 성공", null);
    }

    /**
     * 회원가입 비밀번호 검증
     */
    @PostMapping("/password/confirm")
    public ApiResponse<?> validatePassword(@RequestBody RegisterForm form) {
        // 비밀번호 검증
        memberService.validatePassword(form.getPassword(), form.getPasswordConfirm());

        // 응답
        return ApiResponse.of(ApiResponse.SUCCESS_STATUS, "비밀번호 인증 성공", null);
    }

    /**
     * 회원가입 비밀번호 세팅
     */
    @PostMapping("/password")
    public ApiResponse<?> makePassword(@RequestBody RegisterForm form) {
        // 이메일 한번 더 검증
        Boolean isVerified = memberEmailService.verifyEmail(form.getEmail(), form.getCode());

        if (!isVerified) {
            return ApiResponse.of(ApiResponse.ERROR_STATUS, "이메일 검증 실패", null);
        }

        // 비밀번호 세팅
        memberService.setPassword(form.getEmail(), form.getPassword(), form.getPasswordConfirm());

        // 응답
        return ApiResponse.of(ApiResponse.SUCCESS_STATUS, "비밀번호 설정 성공", null);
    }

    /**
     * 회원정보 입력
     */
    @PostMapping("/profile")
    public ApiResponse<?> makeMemberProfile(@RequestBody RegisterForm registerForm) {
        // 비밀번호 검증
        if (!memberService.passwordMatch(registerForm.getEmail(), registerForm.getPassword())) {
            throw new RuntimeException("비밀번호 안맞음");
        }

        // 프로필 저장
        memberService.modifyMemberProfile(registerForm);

        // 응답
        return ApiResponse.of(ApiResponse.SUCCESS_STATUS, "프로필 저장 완료", null);
    }
}
