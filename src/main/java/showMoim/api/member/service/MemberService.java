package showMoim.api.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import showMoim.api.common.enums.MemberRole;
import showMoim.api.member.dto.MemberJoinDto.RegisterForm;
import showMoim.api.member.entity.Member;
import showMoim.api.member.repository.MemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public final static String ERROR_MESSAGE_EMAIL_EXIST = "이미 존재하는 이메일 입니다.";
    public final static String ERROR_MESSAGE_PW_EMPTY = "비밀번호를 입력하셔야 합니다.";
    public final static String ERROR_MESSAGE_PW_CONFIRM_NOT_MATCHED = "비밀번호가 일치하지 않습니다.";
    public final static String ERROR_MESSAGE_PW_MIN_LENGTH = "비밀번호는 최소 6글자여야 합니다.";

    /**
     * 회원 가입
     */
    public Long join(RegisterForm memberJoinRegisterForm) {
        // 중복 회원 검증
        List<Member> existMember = memberRepository.findByEmail(memberJoinRegisterForm.getEmail());
        if (!existMember.isEmpty()) {
            throw new RuntimeException(ERROR_MESSAGE_EMAIL_EXIST);
        }

        // 멤버 DB 저장
        Member member = memberRepository.save(
            new Member(
                memberJoinRegisterForm.getEmail(),
                passwordEncoder.encode(memberJoinRegisterForm.getPassword()),
                memberJoinRegisterForm.getNickname(),
                MemberRole.ROLE_GUEST.toString() // 최초 Member 생성 시 ROLE_GUSET 권한
            )
        );

        return member.getId();
    }

    /**
     * 비밀번호 검증
     */
    public Boolean validatePassword(String password, String passwordConfirm) {
        // 비밀번호 검증
        if (password == null || passwordConfirm == null) {
            throw new RuntimeException(ERROR_MESSAGE_PW_EMPTY);
        }

        if (password.length() < 6) {
            throw new RuntimeException(ERROR_MESSAGE_PW_MIN_LENGTH);
        }

        if (!password.equals(passwordConfirm)) {
            throw new RuntimeException(ERROR_MESSAGE_PW_CONFIRM_NOT_MATCHED);
        }

        return true;
    }

    /**
     * 비밀번호 설정
     */
    public void setPassword(String email, String password, String passwordConfirm) {
        // 비밀번호 재검증
        if (!validatePassword(password, passwordConfirm)) return;

        Member member = memberRepository.findByEmail(email).stream().findFirst().orElse(null);

        if (member == null) {
            throw new RuntimeException("");
        }

        // 비밀번호 업데이트
        member.updatePassword(passwordEncoder.encode(password));
    }

    /**
     * 비밀번호 맞는지 검사
     */
    public Boolean passwordMatch(String email, String password) {
        Member member = memberRepository.findByEmail(email).stream().findFirst().orElse(null);

        if (member == null) {
            throw new RuntimeException("");
        }

        // 비밀번호 체크
        return passwordEncoder.matches(password, member.getPassword());
    }

    /**
     * 프로필 저장
     */
    public void modifyMemberProfile(RegisterForm registerForm) {
        Member member = memberRepository.findByEmail(registerForm.getEmail()).stream().findFirst().orElse(null);

        if (member == null) {
            throw new RuntimeException("");
        }

        // 프로필 검증로직 필요

        // 프로필 업데이트
        member.updateProfile(registerForm);
    }
}
