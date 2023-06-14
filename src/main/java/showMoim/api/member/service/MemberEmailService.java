package showMoim.api.member.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import showMoim.api.common.enums.MemberRole;
import showMoim.api.member.dto.MemberJoinDto.RegisterForm;
import showMoim.api.member.entity.Member;
import showMoim.api.member.repository.MemberRepository;

/**
 * Member 이메일 관련 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional
public class MemberEmailService {
    private final MemberRepository memberRepository;

    public final static String ERROR_MESSAGE_EMAIL_VERIFY_FAILED = "이메일 인증에 실패했습니다.";
    public final static String ERROR_MESSAGE_EMPTY_EMAIL_VALUE = "이메일 값이 없습니다.";
    public final static String ERROR_MESSAGE_EMAIL_NOT_EXIST = "해당 이메일의 회원이 존재하지 않습니다.";



    /**
     * 이메일 인증 메일 발송
     */
    public void sendVerificationEmailCode(String email) {
        // dbless 방식??
        // 어떻게 할지 고민좀 해봐야할듯
        return;
    }

    /**
     * 이메일 인증 코드 검증
     */
    public Boolean verifyEmail(String email, String code) {
        if (email == null) {
            throw new RuntimeException(ERROR_MESSAGE_EMPTY_EMAIL_VALUE);
        }

        // 코드 검증 로직 들어가야함 (코드 검증, 만료시간 체크 등등)

        // 인증 실패 시
        // throw new RuntimeException(ERROR_MESSAGE_EMAIL_VERIFY_FAILED);

        // 검증 완료 시 권한 업데이트
        Member member = memberRepository.findByEmail(email).stream().findFirst().orElse(null);

        if (member == null) {
            throw new RuntimeException(ERROR_MESSAGE_EMAIL_NOT_EXIST);
        }

        member.addRole(MemberRole.ROLE_MEMBER);

        return true;
    }
}
