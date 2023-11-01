package showMoim.api.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import showMoim.api.global.common.ErrorCode;
import showMoim.api.global.common.ShowMoimException;
import showMoim.api.domain.member.repository.MemberRepository;

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
            throw new RuntimeException();
        }

        // 코드 검증 로직 들어가야함 (코드 검증, 만료시간 체크 등등)
        if (!"123456".equals(code)) {
            throw new ShowMoimException(ErrorCode.EMAIL_VERIFY_FAILED);
        }

        return true;
    }
}
