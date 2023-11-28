package showMoim.api.group.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static showMoim.api.domain.member.service.MemberService.ERROR_MESSAGE_EMAIL_EXIST;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import showMoim.api.domain.group.dto.GroupDto;
import showMoim.api.domain.group.dto.GroupDto.GroupCreateForm;
import showMoim.api.domain.group.dto.GroupDto.GroupJoinForm;
import showMoim.api.domain.group.entity.Group;
import showMoim.api.domain.group.entity.GroupJoinRequest;
import showMoim.api.domain.group.repository.GroupRepository;
import showMoim.api.domain.group.service.GroupMemberService;
import showMoim.api.domain.group.service.GroupService;
import showMoim.api.domain.member.entity.Member;
import showMoim.api.domain.member.repository.MemberRepository;
import showMoim.api.global.common.ErrorCode;

@SpringBootTest
@Transactional
class GroupMemberServiceTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    GroupService groupService;

    @Autowired
    GroupMemberService groupMemberService;

    @Test
    public void 그룹가입요청_테스트() {
        Member member1 = new Member("hello1@asd.com", "1234", "hello1");
        Member member2 = new Member("hello2@asd.com", "1234", "hello2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        Group group = groupService.createGroup(new GroupCreateForm("asd", "asd", 1L), member1);

        // when
        // 이미 가입 -> 예외
        Executable joinWithExistMember = () -> groupMemberService.sendGroupJoinRequest(new GroupJoinForm(group.getId(), "msg"), member1);
        // 가입 X -> 정상 가입요청
        GroupJoinRequest groupJoinRequest = groupMemberService.sendGroupJoinRequest(new GroupJoinForm(group.getId(), "msg"), member2);

        // then
        RuntimeException e = assertThrows(RuntimeException.class, joinWithExistMember);
        assertThat(e.getMessage()).contains(ErrorCode.MEMBER_ALREADY_EXIST_IN_GROUP.getMessage());
        assertThat(groupJoinRequest.getMember().getId()).isEqualTo(member2.getId());
    }
}