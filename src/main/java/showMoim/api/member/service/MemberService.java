package showMoim.api.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import showMoim.api.member.dto.MemberJoinDto;
import showMoim.api.member.entity.Member;
import showMoim.api.member.repository.MemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public final static String ERROR_MESSAGE_EMAIL_EXIST = "이미 존재하는 이메일 입니다.";
    public final static String ERROR_MESSAGE_PW_CONFIRM_NOT_MATCHED = "비밀번호가 일치하지 않습니다.";
    public final static String ERROR_MESSAGE_PW_MIN_LENGTH = "비밀번호는 최소 6글자여야 합니다.";

    /**
     * 회원 가입
     */
    public Long join(MemberJoinDto.Form memberJoinForm) {
        // 중복 회원 검증
        List<Member> existMember = memberRepository.findByEmail(memberJoinForm.getEmail());
        if (!existMember.isEmpty())
            throw new RuntimeException(ERROR_MESSAGE_EMAIL_EXIST);

        // 비밀번호 검증
        if (!memberJoinForm.getPassword().equals(memberJoinForm.getPasswordConfirm()))
            throw new RuntimeException(ERROR_MESSAGE_PW_CONFIRM_NOT_MATCHED);

        if (memberJoinForm.getPassword().length() < 6)
            throw new RuntimeException(ERROR_MESSAGE_PW_MIN_LENGTH);


        // 멤버 DB 저장
        Member member = memberRepository.save(
                new Member(
                        memberJoinForm.getEmail(),
                        memberJoinForm.getPassword(),
                        memberJoinForm.getNickname()
                )
        );

        return member.getId();
    }
}
