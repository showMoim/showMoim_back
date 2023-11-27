package showMoim.api.domain.group.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import showMoim.api.domain.group.entity.Group;
import showMoim.api.domain.group.repository.GroupRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupService {
    private final GroupRepository groupRepository;

    public void createGroup() {

    }

    public List<Group> searchGroup() {
        return null;
    }
}
