package showMoim.api.member.dto;

import lombok.*;

public class MemberJoinDto {
    /**
     * 회원가입 Form DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Form {
        private String email;

        private String nickname;

        private String password;

        private String passwordConfirm;
    }
}
