package showMoim.api.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import showMoim.api.common.ErrorCode;
import showMoim.api.common.ShowMoimException;
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
    public Long join(RegisterForm form) {
        // 중복 회원 검증
        List<Member> existMember = memberRepository.findByEmail(form.getEmail());
        if (!existMember.isEmpty()) {
            throw new ShowMoimException(ErrorCode.EMAIL_ALREADY_EXIST);
        }

        // 비밀번호 검증
        this.validatePassword(form.getPassword(), form.getPasswordConfirm());

        // 프로필 저장
        this.modifyMemberProfile(form.getEmail(), form.getNickname());

        // 멤버 DB 저장
        Member member = memberRepository.save(
            new Member(
                form.getEmail(),
                passwordEncoder.encode(form.getPassword()),
                form.getNickname(),
                MemberRole.ROLE_MEMBER.toString() // 가입 시 메일 인증 받았으므로 ROLE_MEMBER 로 저장
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
            throw new ShowMoimException(ErrorCode.PASSWORD_EMPTY);
        }

        if (password.length() < 6) {
            throw new ShowMoimException(ErrorCode.PASSWORD_TOO_SHORT);
        }

        if (!password.equals(passwordConfirm)) {
            throw new ShowMoimException(ErrorCode.PASSWORD_NOT_CONFIRMED);
        }

        return true;
    }

    /**
     * 비밀번호 설정
     */
    public void setPassword(String email, String password, String passwordConfirm) {
        // 비밀번호 재검증
        if (!validatePassword(password, passwordConfirm)) {
            return;
        }

        Member member = memberRepository.findByEmail(email).stream().findFirst().orElse(null);

        if (member == null) {
            throw new ShowMoimException(ErrorCode.MEMBER_NOT_EXIST);
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
            throw new ShowMoimException(ErrorCode.PASSWORD_INCORRECT);
        }

        // 비밀번호 체크
        return passwordEncoder.matches(password, member.getPassword());
    }

    /**
     * 프로필 저장
     */
    public void modifyMemberProfile(String email, String nickname) {
        Member member = memberRepository.findByEmail(email).stream().findFirst().orElse(null);

        if (member == null) {
            throw new ShowMoimException(ErrorCode.MEMBER_NOT_EXIST);
        }

        // 이후 요건에 따라 프로필 검증로직 필요
        if (nickname == null) {
            throw new ShowMoimException(ErrorCode.NICKNAME_INVALID);
        }

        // 프로필 업데이트
        member.updateProfile(nickname);
    }
}
