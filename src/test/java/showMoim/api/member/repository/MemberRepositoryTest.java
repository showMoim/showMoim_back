package showMoim.api.member.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import showMoim.api.domain.member.entity.Member;
import showMoim.api.domain.member.repository.MemberRepository;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired
    EntityManager em;
    @Autowired
    MemberRepository memberRepository;

    // 각 테스트 전에 실행
    @BeforeEach
    public void before() {
        Member member1 = new Member("hello1@asd.com", "1234", "hello1");
        Member member2 = new Member("hello2@asd.com", "1234", "hello2");

        memberRepository.save(member1);
        memberRepository.save(member2);
    }

    @Test
    public void 사용자_추가_테스트() {
        // given
        Member savedMember = memberRepository.save(new Member("hello3@asd.com", "1234", "hello3"));

        // when
        Member resultMember = memberRepository.findById(savedMember.getId()).orElseThrow(IllegalAccessError::new);

        // then
        assertThat(resultMember).isEqualTo(savedMember);
    }

    @Test
    public void 닉네임_이메일_통합검색_테스트() {
        // given
        memberRepository.save(new Member("test1@닉네임_이메일_통합검색_테스트.com", "1234", "hello3"));
        memberRepository.save(new Member("닉네임_이메일_통합검색_테스트@asd.com", "1234", "hello4"));
        memberRepository.save(new Member("asdasd@as닉네임_이메일_통합검색_테스트d.com", "1234", "TeSt5"));

        // when
        int resultSize = memberRepository.search("닉네임_이메일_통합검색_테스트").size();

        // then
        assertThat(resultSize).isEqualTo(3);
    }
}