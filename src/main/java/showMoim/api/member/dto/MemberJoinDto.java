package showMoim.api.member.dto;

import lombok.*;

public class MemberJoinDto {
    /**
     * 회원가입 관련 DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterForm {
        private String email;

        private String code;

        private String nickname;

        private String password;

        private String passwordConfirm;
    }
}
