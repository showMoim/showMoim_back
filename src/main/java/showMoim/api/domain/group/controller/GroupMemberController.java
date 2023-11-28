package showMoim.api.domain.group.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import showMoim.api.domain.group.dto.GroupDto.GroupJoinAccept;
import showMoim.api.domain.group.dto.GroupDto.GroupJoinForm;
import showMoim.api.domain.group.service.GroupMemberService;
import showMoim.api.domain.member.entity.Member;
import showMoim.api.global.common.ApiResponse;
import showMoim.api.global.security.auth.PrincipalDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/group/member", produces = MediaType.APPLICATION_JSON_VALUE)
public class GroupMemberController {

    private final GroupMemberService groupMemberService;

    /**
     * 모임 가입 요청
     */
    @PostMapping("/join")
    public ApiResponse<?> requestJoinGroup(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                           @RequestBody GroupJoinForm groupJoinForm) {

        Member member = principalDetails.getMember();

        // 가입 요청
        groupMemberService.sendGroupJoinRequest(groupJoinForm, member);

        return ApiResponse.of(ApiResponse.SUCCESS_STATUS, "모임 가입 요청 성공", null);
    }

    /**
     * 모임 가입 요청 수락
     */
    @PostMapping("/join/accept")
    public ApiResponse<?> acceptJoinGroup(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                          @RequestBody GroupJoinAccept groupJoinAccept) {

        Member member = principalDetails.getMember();

        groupMemberService.acceptGroupJoinRequest(groupJoinAccept.getGroupJoinRequestId(), member);

        return ApiResponse.of(ApiResponse.SUCCESS_STATUS, "모임 가입 수락 완료", null);
    }
}
