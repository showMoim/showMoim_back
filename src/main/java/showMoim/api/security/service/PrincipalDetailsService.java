package showMoim.api.security.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import showMoim.api.member.entity.Member;
import showMoim.api.member.repository.MemberRepository;
import showMoim.api.security.auth.PrincipalDetails;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 로그인은 이메일로 이루어지므로 메일 주소 기준으로 가져옴
        List<Member> members = memberRepository.findByEmail(username);

        // 멤버 로직 처리
        // Enable 여부, Ban 여부, 탈퇴 여부 등등 판단
        if (members.size() == 0) throw new UsernameNotFoundException("해당되는 계정이 없음");

        return new PrincipalDetails(members.get(0));
    }
}
