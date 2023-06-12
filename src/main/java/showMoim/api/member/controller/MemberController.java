package showMoim.api.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import showMoim.api.common.ApiResponse;
import showMoim.api.member.dto.MemberJoinDto.RegisterForm;
import showMoim.api.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/member", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberController {

    private final MemberService memberService;


    /**
     * 회원가입 API
     */
    @PostMapping("/join")
    public ApiResponse<?> createMember(@RequestParam String email,
                                       @RequestParam String nickname,
                                       @RequestParam String password,
                                       @RequestParam String passwordConfirm) {

        // 가입 폼 DTO 빌드
        RegisterForm registerForm = RegisterForm.builder()
            .email(email)
            .nickname(nickname)
            .password(password)
            .passwordConfirm(passwordConfirm)
            .build();

        // 가입 로직 호출
        memberService.join(registerForm);

        // 응답
        return ApiResponse.of(ApiResponse.SUCCESS_STATUS, "회원가입 성공", null);
    }

    @GetMapping("token")
    public ApiResponse<String> tokenTest() {
        return ApiResponse.of(ApiResponse.SUCCESS_STATUS, "토큰 인증 성공", null);
    }
}
