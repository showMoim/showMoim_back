package showMoim.api.domain.group.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import showMoim.api.domain.group.entity.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Slice<Group> findByTitleContainsIgnoreCaseOrderByCreatedAtDesc(String title, Pageable pageable);
}
