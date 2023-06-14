package showMoim.api.member.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import showMoim.api.member.dto.MemberJoinDto.RegisterForm;
import showMoim.api.member.entity.Member;
import showMoim.api.member.repository.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static showMoim.api.member.service.MemberService.ERROR_MESSAGE_EMAIL_EXIST;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;


    @Test
    void 회원가입_중복_이메일_테스트() {
        // given
        String existEmail = "exist@naver.com";
        memberRepository.save(new Member(existEmail, "1234", "nickname"));

        RegisterForm registerForm = RegisterForm.builder()
                .email(existEmail)
                .build();

        // when
        Executable joinWithExistEmail = () -> memberService.join(registerForm);

        // then
        RuntimeException e = assertThrows(RuntimeException.class, joinWithExistEmail);
        assertThat(e.getMessage()).contains(ERROR_MESSAGE_EMAIL_EXIST);
    }
}