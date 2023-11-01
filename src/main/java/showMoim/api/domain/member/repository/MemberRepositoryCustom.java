package showMoim.api.domain.member.repository;

import showMoim.api.domain.member.entity.Member;

import java.util.List;

/**
 * 사용자 지정 쿼리 구현
 * MemberRepositoryImpl 에 구현되어 있음
 */
public interface MemberRepositoryCustom {
    public List<Member> search(String keyword);
}
