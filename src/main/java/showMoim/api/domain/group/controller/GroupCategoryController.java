package showMoim.api.domain.group.controller;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import showMoim.api.domain.group.entity.GroupCategory;
import showMoim.api.domain.group.repository.GroupCategoryRepository;
import showMoim.api.global.common.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/group/category", produces = MediaType.APPLICATION_JSON_VALUE)
public class GroupCategoryController {

    private final GroupCategoryRepository groupCategoryRepository;

    /**
     * 모임 분야 조회
     */

    @GetMapping("")
    public ApiResponse<?> getGroupCategoryList() {
        List<GroupCategory> all = groupCategoryRepository.findAll();

        return ApiResponse.of(ApiResponse.SUCCESS_STATUS, "카테고리 조회 완료", all);
    }

}
