package showMoim.api.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import showMoim.api.member.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

import static showMoim.api.member.entity.QMember.member;

/**
 * 커스텀 MemberRepository
 * QueryDSL 을 사용해서 원하는 대로 직접 쿼리
 */
@Slf4j
@Repository
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 닉네임, 이메일 통합 검색 예제
     */
    public List<Member> search(String keyword) {
        // QueryDSL 을 쓰게되면 아래처럼 쿼리문을 자바코드로 작성가능
        return queryFactory
                .select(member)
                .from(member)
                .where(member.nickname.likeIgnoreCase("%" + keyword + "%")
                        .or(member.email.likeIgnoreCase("%" + keyword + "%")))
                .fetch();
    }

}
