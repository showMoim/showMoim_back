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
     * 토큰으로 인증여부 검증
     */
    @GetMapping("/auth")
    public ApiResponse<String> checkAuth() {
        return ApiResponse.of(ApiResponse.SUCCESS_STATUS, "토큰 인증 성공", null);
    }
}
