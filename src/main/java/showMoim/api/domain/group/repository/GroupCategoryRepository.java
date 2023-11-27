package showMoim.api.domain.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import showMoim.api.domain.group.entity.GroupCategory;

public interface GroupCategoryRepository extends JpaRepository<GroupCategory, Long> {

}
