package showMoim.api.domain.group.service;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import showMoim.api.domain.group.dto.GroupDto.GroupJoinForm;
import showMoim.api.domain.group.entity.Group;
import showMoim.api.domain.group.entity.GroupJoinRequest;
import showMoim.api.domain.group.entity.GroupMember;
import showMoim.api.domain.group.repository.GroupJoinRequestRepository;
import showMoim.api.domain.group.repository.GroupMemberRepository;
import showMoim.api.domain.group.repository.GroupRepository;
import showMoim.api.domain.member.entity.Member;
import showMoim.api.global.common.ErrorCode;
import showMoim.api.global.common.ShowMoimException;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupMemberService {
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupJoinRequestRepository groupJoinRequestRepository;

    public GroupJoinRequest sendGroupJoinRequest(GroupJoinForm groupJoinForm, Member member) {
        Group group = groupRepository
            .findById(groupJoinForm.getGroupId())
            .orElseThrow(() -> new ShowMoimException(ErrorCode.GROUP_NOT_EXIST));

        // 이미 그룹에 존재하는 경우 체크
        List<GroupMember> groupMemberList = groupMemberRepository.findByMember(member);
        if (groupMemberList.size() > 0) {
            throw new ShowMoimException(ErrorCode.MEMBER_ALREADY_EXIST_IN_GROUP);
        }

        // 이미 그룹에 가입요청 보낸 경우 체크
        List<GroupJoinRequest> groupJoinRequestList = groupJoinRequestRepository.findByMemberAndGroup(member, group);
        if (groupJoinRequestList.size() > 0) {
            throw new ShowMoimException(ErrorCode.MEMBER_ALREADY_SENT_GROUP_JOIN_REQUEST);
        }

        GroupJoinRequest groupJoinRequest = GroupJoinRequest.builder()
            .member(member)
            .group(group)
            .message(groupJoinForm.getMessage())
            .build();

        GroupJoinRequest request = groupJoinRequestRepository.save(groupJoinRequest);

        return request;
    }
}
