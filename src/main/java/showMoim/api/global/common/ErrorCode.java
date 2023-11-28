package showMoim.api.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 에러 목록
 */
@AllArgsConstructor
@Getter
public enum ErrorCode {

    // 멤버 관련
    AUTH_FAILED("ERR000", "로그인에 실패했습니다."),
    MEMBER_NOT_EXIST("ERR001", "존재하지 않는 회원입니다."),
    EMAIL_ALREADY_EXIST("ERR002", "이미 존재하는 메일입니다."),
    PASSWORD_INVALID("ERR003", "비밀번호 형식에 맞지 않습니다."),
    PASSWORD_INCORRECT("ERR004", "비밀번호가 다릅니다."),
    EMAIL_VERIFY_FAILED("ERR005", "이메일 인증에 실패했습니다."),
    PASSWORD_EMPTY("ERR006", "비밀번호 입력값이 비었습니다."),
    PASSWORD_TOO_SHORT("ERR007", "비밀번호가 짧습니다."),
    PASSWORD_NOT_CONFIRMED("ERR008", "비밀번호 확인과 일치하지 않습니다."),
    NICKNAME_INVALID("ERR008", "올바르지 않은 닉네임입니다."),

    // 그룹 관련
    GROUP_CATEGORY_INVALID("ERR009", "존재하지 않는 카테고리 입니다."),
    GROUP_NOT_EXIST("ERR010", "존재하지 않는 그룹 입니다."),
    MEMBER_ALREADY_EXIST_IN_GROUP("ERR011", "이미 그룹에 존재하는 회원입니다."),
    MEMBER_ALREADY_SENT_GROUP_JOIN_REQUEST("ERR011", "이미 그룹에 가입 요청을 보낸 회원입니다."),


    // 공통 에러
    ID_NOT_EXIST("ERR900", "존재하지 않는 ID 입니다."),
    UNAUTHORIZED("ERR901", "권한이 없습니다."),
    ;

    private final String code;
    private final String message;
}
