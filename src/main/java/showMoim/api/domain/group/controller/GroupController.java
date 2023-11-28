package showMoim.api.domain.group.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import showMoim.api.domain.group.dto.GroupDto.GroupCreateForm;
import showMoim.api.domain.group.entity.Group;
import showMoim.api.domain.group.service.GroupService;
import showMoim.api.domain.member.entity.Member;
import showMoim.api.global.common.ApiResponse;
import showMoim.api.global.common.utils.Pagination;
import showMoim.api.global.security.auth.PrincipalDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/group", produces = MediaType.APPLICATION_JSON_VALUE)
public class GroupController {
    private final GroupService groupService;

    /**
     * 모임 만들기
     */
    @PostMapping("/create")
    public ApiResponse<?> createGroup(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                      @RequestBody GroupCreateForm groupCreateForm) {

        Member member = principalDetails.getMember();

        // 그룹 생성
        Group group = groupService.createGroup(groupCreateForm, member);

        return ApiResponse.of(ApiResponse.SUCCESS_STATUS, "모임 생성 성공", group);
    }


    /**
     * 모임 목록 조회
     */
    @GetMapping("/list")
    public ApiResponse<?> getGroupList(@RequestParam(value = "page", defaultValue = "1") int page,
                                       @RequestParam(value = "size", defaultValue = "10") int size,
                                       @RequestParam(value = "keyword", defaultValue = "") String keyword) {

        Slice<Group> groupSlice = groupService.getGroupList(keyword, Pagination.of(page, size));

        return ApiResponse.of(ApiResponse.SUCCESS_STATUS, "검색 성공", groupSlice);
    }
}
