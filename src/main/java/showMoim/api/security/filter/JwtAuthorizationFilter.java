package showMoim.api.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import showMoim.api.member.entity.Member;
import showMoim.api.member.repository.MemberRepository;
import showMoim.api.security.JwtProperties;
import showMoim.api.security.auth.PrincipalDetails;

/**
 * 권한이나 인증이 필요한 주소에 요청하면 이 필터를 탐
 */
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final MemberRepository memberRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository) {
        super(authenticationManager);
        this.memberRepository = memberRepository;
    }

    /**
     * 토큰 검증 로직
     * <p>
     * Authorization 헤더에서 Access Token 검증
     * Access Token 검증에 실패하면 쿠키에서 Refresh Token 검증, 성공 시 Access Token 재발급 후 헤더에 추가해서 응답
     */
    private void tokenAuthorization(HttpServletRequest request, HttpServletResponse response) {
        Long id = null;

        // 1. Access Token 추출
        String accessToken = JwtProperties.extractAccessToken(request.getHeader(JwtProperties.HEADER_STRING));

        // 2. Access Token 검증
        if (!JwtProperties.validateAccessToken(accessToken)) {
            // 3. Access Token 검증 실패 시, Refresh Token 검증
            String refreshToken = JwtProperties.extractRefreshToken(request.getCookies());

            // 3-1. Refresh Token 검증 실패 시 -> 로그인 실패
            if (!JwtProperties.validateRefreshToken(refreshToken)) return;

            // 4. Refresh Token 토큰 검증 성공 시, Access Token 재발급
            log.debug("Refresh Token 재발급");
            id = JWT.require(Algorithm.HMAC256(JwtProperties.SECRET))
                    .build()
                    .verify(refreshToken)
                    .getClaim("id")
                    .asLong();

            String email = JWT.require(Algorithm.HMAC256(JwtProperties.SECRET))
                    .build()
                    .verify(refreshToken)
                    .getClaim("email")
                    .asString();

            accessToken = JwtProperties.createToken(JwtProperties.TokenType.ACCESS_TOKEN, id, email);

            // Authorization 헤더에 새로운 Access Token 추가
            response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken);

        }

        if (id == null) return;

        Member member = memberRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        PrincipalDetails principalDetails = new PrincipalDetails(member);

        // 인증에 성공하면 Authentication 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

        log.debug("토큰 인증 성공");
        // 시큐리티 콘텍스트에 Authentication 객체 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        tokenAuthorization(request, response);
        chain.doFilter(request, response);
    }
}
