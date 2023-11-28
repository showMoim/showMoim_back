package showMoim.api.domain.group.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import showMoim.api.domain.group.dto.GroupDto.GroupCreateForm;
import showMoim.api.domain.group.entity.Group;
import showMoim.api.domain.group.entity.GroupCategory;
import showMoim.api.domain.group.entity.GroupMember;
import showMoim.api.domain.group.repository.GroupCategoryRepository;
import showMoim.api.domain.group.repository.GroupJoinRequestRepository;
import showMoim.api.domain.group.repository.GroupMemberRepository;
import showMoim.api.domain.group.repository.GroupRepository;
import showMoim.api.domain.member.entity.Member;
import showMoim.api.global.common.ErrorCode;
import showMoim.api.global.common.ShowMoimException;
import showMoim.api.global.common.enums.GroupRole;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupCategoryRepository groupCategoryRepository;

    public Group createGroup(GroupCreateForm groupCreateForm, Member admin) {
        // 카테고리 조회
        GroupCategory groupCategory = groupCategoryRepository
            .findById(groupCreateForm.getGroupCategoryId())
            .orElseThrow(() -> new ShowMoimException(ErrorCode.GROUP_CATEGORY_INVALID));

        // 그룹 생성
        Group group = Group.builder()
            .title(groupCreateForm.getTitle())
            .description(groupCreateForm.getDescription())
            .groupCategory(groupCategory)
            .build();

        // 생성 요청 멤버를 그룹 어드민으로 추가
        GroupMember groupMember = GroupMember
            .builder()
            .group(group)
            .member(admin)
            .groupRole(GroupRole.GROUP_ADMIN)
            .build();

        groupMemberRepository.save(groupMember);

        return groupRepository.save(group);
    }

    public Slice<Group> getGroupList(String keyword, Pageable pageable) {
        return groupRepository.findByTitleContainsIgnoreCase(keyword, pageable);
    }
}
