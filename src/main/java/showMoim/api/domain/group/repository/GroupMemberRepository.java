package showMoim.api.domain.group.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import showMoim.api.domain.group.entity.GroupMember;
import showMoim.api.domain.member.entity.Member;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    List<GroupMember> findByMember(Member member);
}
