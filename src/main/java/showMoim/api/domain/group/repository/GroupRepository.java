package showMoim.api.domain.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import showMoim.api.domain.group.entity.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

}
