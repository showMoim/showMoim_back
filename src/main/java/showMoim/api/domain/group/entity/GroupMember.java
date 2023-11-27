package showMoim.api.domain.group.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import showMoim.api.domain.member.entity.Member;
import showMoim.api.global.common.enums.GroupRole;

/**
 * 모임-멤버 연결 테이블
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Group group;

    @Enumerated(EnumType.STRING)
    private GroupRole groupRole;
}
