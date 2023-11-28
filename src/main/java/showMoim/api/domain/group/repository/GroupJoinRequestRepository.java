package showMoim.api.domain.group.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import showMoim.api.domain.group.entity.Group;
import showMoim.api.domain.group.entity.GroupJoinRequest;
import showMoim.api.domain.member.entity.Member;

public interface GroupJoinRequestRepository extends JpaRepository<GroupJoinRequest, Long> {
    List<GroupJoinRequest> findByMemberAndGroup(Member member, Group group);

}
