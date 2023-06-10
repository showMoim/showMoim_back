package showMoim.api.member.entity;
import javax.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import showMoim.api.common.enums.MemberRole;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;

    private String password;

    private String nickname;

    private String roles;

    public Member(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        // 최초 Member 생성 시 ROLE_GUSET 권한
        this.roles = MemberRole.ROLE_GUEST.toString();
    }

    /**
     * 콤마로 구분된 roles 문자열을 읽어서 MemberRole 의 List 로 변환
     */
    public List<MemberRole> getRoleList() {
        List<MemberRole> roleList = new ArrayList<>();

        if (this.roles.length() > 0) {
            String[] roleStrings = this.roles.split(",");

            for (String roleString : roleStrings) {
                MemberRole role = MemberRole.valueOf(roleString.trim());
                roleList.add(role);
            }
        }

        return roleList;
    }
}
