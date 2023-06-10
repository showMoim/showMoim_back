package showMoim.api.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import showMoim.api.member.entity.Member;

import java.util.List;

// JpaRepository 를 상속받으면 findAll, findById 등등 기본적인 쿼리들을 자동으로 구현해줌
// MemberRepositoryCustom 을 상속받아서 Jpa 구현체 이외에 직접 필요한 쿼리들을 구현해서 사용
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    // 이런식으로 인터페이스안에 메소드를 선언해주면 Jpa 가 자동으로 구현해줌

    /**
     * 이메일로 Member 조회
     */
    public List<Member> findByEmail(String email);
}
