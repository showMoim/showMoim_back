package showMoim.api.domain.group.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import showMoim.api.domain.group.service.GroupService;
import showMoim.api.global.common.ApiResponse;
import showMoim.api.global.security.auth.PrincipalDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/group", produces = MediaType.APPLICATION_JSON_VALUE)
public class GroupController {
    private final GroupService groupService;

    /**
     * 모임 만들기
     */
    @GetMapping("/create")
    public ApiResponse<?> createGroup(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("[principalDetails]: " + principalDetails.getUsername());
        return ApiResponse.of(ApiResponse.SUCCESS_STATUS, "모임 생성 성공", null);
    }
}
