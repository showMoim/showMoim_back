package showMoim.api.domain.member.entity;

import com.google.common.base.Joiner;
import javax.persistence.*;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

import showMoim.api.global.common.enums.MemberRole;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Table(name = "showmoim_member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;

    private String password;

    private String nickname;

    private String roles;


    public Member(String email, String password, String nickname, String roles) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.roles = roles;
    }

    public Member(String email, String password, String nickname) {
        // Role 안주면 GUEST 권한으로 기본 생성
        this(email, password, nickname, MemberRole.ROLE_GUEST.toString());
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

    /**
     * 권한 추가 메소드
     */
    public void addRole(MemberRole role) {
        if (!this.getRoleList().contains(role)) {
            List<MemberRole> roleList = getRoleList();
            roleList.add(role);

            this.roles = Joiner.on(',').join(roleList);
        }
    }

    /**
     * 비밀번호 업데이트
     */
    public void updatePassword(String password) {
        this.password = password;
    }

    /**
     * 사용자 정보 업데이트
     */
    public void updateProfile(String nickname) {
        this.nickname = nickname;
    }
}
