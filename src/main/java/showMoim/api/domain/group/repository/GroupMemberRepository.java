package showMoim.api.domain.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import showMoim.api.domain.group.entity.GroupMember;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

}
