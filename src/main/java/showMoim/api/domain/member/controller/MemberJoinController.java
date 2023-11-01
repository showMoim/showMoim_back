package showMoim.api.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import showMoim.api.global.common.ApiResponse;
import showMoim.api.domain.member.dto.MemberJoinDto.ChangePasswordForm;
import showMoim.api.domain.member.dto.MemberJoinDto.RegisterForm;
import showMoim.api.domain.member.service.MemberEmailService;
import showMoim.api.domain.member.service.MemberService;

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
     * 회원가입 인증 메일 요청 API (DBless 형태?)
     */
    @PostMapping("/email/verify/send")
    public ApiResponse<?> sendVerificationEmail(@RequestBody RegisterForm form) {
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
     * 회원가입 API
     */
    @PostMapping("")
    public ApiResponse<?> join(@RequestBody RegisterForm registerForm) {
        // 이메일 한번 더 검증
        memberEmailService.verifyEmail(registerForm.getEmail(), registerForm.getCode());

        // 회원가입
        memberService.join(registerForm);

        // 응답
        return ApiResponse.of(ApiResponse.SUCCESS_STATUS, "회원가입 완료", null);
    }

    /**
     * 비밀번호 재설정 API
     */
    @PostMapping("/change/password")
    public ApiResponse<?> changePassword(@RequestBody ChangePasswordForm changePasswordForm) {
        // 이메일 한번 더 검증
        memberEmailService.verifyEmail(changePasswordForm.getEmail(), changePasswordForm.getCode());

        // 기존 비밀번호 체크
        if (memberService.passwordMatch(changePasswordForm.getEmail(), changePasswordForm.getPreviousPassword())) {
            // 비밀번호 맞으면 변경
            memberService.setPassword(
                changePasswordForm.getEmail(),
                changePasswordForm.getNewPassword(),
                changePasswordForm.getNewPasswordConfirm()
            );
        }

        // 응답
        return ApiResponse.of(ApiResponse.SUCCESS_STATUS, "비밀번호 변경 완료", null);
    }
}
