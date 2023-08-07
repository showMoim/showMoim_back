package showMoim.api.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import showMoim.api.common.ApiResponse;
import showMoim.api.member.dto.MemberLoginDto;
import showMoim.api.security.JwtProperties;
import showMoim.api.security.auth.PrincipalDetails;

/**
 * 로그인 요청해서 username, password 전송하면 (POST) UsernamePasswordAuthenticationFilter 가 동작
 */

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    /**
     * 로그인 요청 시 실행되는 함수
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper om = new ObjectMapper();

        try {
            MemberLoginDto.Form loginFormDto = om.readValue(request.getInputStream(), MemberLoginDto.Form.class);

            //  1. PrincipalDetails 의 loadUserByUsername 호출
            //  2. PrincipalDetailsService 에서 DB 조회
            //  3. DB 가져온 사용자랑 이메일, 비번 비교
            //  4. 성공시 Authentication 객체를 만들어서 필터 체인으로 리턴
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginFormDto.getEmail(), loginFormDto.getPassword());

            return authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            // 로그인 실패  시 응답
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            ApiResponse<?> responseBody = ApiResponse.of(ApiResponse.ERROR_STATUS, e.getMessage(), null);

            try {
                new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            e.printStackTrace();
        }

        return null;
    }


    /**
     * attemptAuthentication 이후, 인증 성공 시 실행되는 함수 JWT 토큰을 만들어서 응답함
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // Access Token 생성
        String accessToken = JwtProperties.createToken(
                JwtProperties.TokenType.ACCESS_TOKEN,
                principalDetails.getMember().getId(),
                principalDetails.getMember().getEmail()
        );

        // Refresh Token 생성
        String refreshToken = JwtProperties.createToken(
                JwtProperties.TokenType.REFRESH_TOKEN,
                principalDetails.getMember().getId(),
                principalDetails.getMember().getEmail()
        );

        // Access Token 쿠키 발급
        Cookie accessTokenCookie = JwtProperties.createCookie(JwtProperties.TokenType.ACCESS_TOKEN, accessToken, false);
        response.addCookie(accessTokenCookie);

        // Refresh Token 쿠키 발급
        Cookie refreshTokenCookie = JwtProperties.createCookie(JwtProperties.TokenType.REFRESH_TOKEN, refreshToken, true);
        response.addCookie(refreshTokenCookie);


        // 로그인 성공 시 응답
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<?> responseBody = ApiResponse.of(ApiResponse.SUCCESS_STATUS, "로그인 성공", null);

        new ObjectMapper().writeValue(response.getOutputStream(), responseBody);

        log.debug("로그인 성공");
    }
}

