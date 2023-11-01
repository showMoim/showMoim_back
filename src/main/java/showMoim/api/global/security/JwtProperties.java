package showMoim.api.global.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Date;

/**
 * JWT 관련 속성 정의
 */
public class JwtProperties {
    public final static String SECRET = "showMoim-credit!23"; // JWT 암호화 시크릿 키
    public final static int ACCESS_TOKEN_EXPIRATION_TIME = 60 * 60 * 1000; // 1시간 (milliseconds 단위)
    public final static int REFRESH_TOKEN_EXPIRATION_TIME = 10 * 24 * 60 * 60 * 1000; // 10일 (milliseconds 단위)
    public final static String TOKEN_PREFIX = "Bearer ";
    public final static String HEADER_STRING = "Authorization";

    public final static String ACCESS_TOKEN_COOKIE_NAME = "SHOWMOIM_ACCESS_TOKEN";
    public final static String REFRESH_TOKEN_COOKIE_NAME = "SHOWMOIM_REFRESH_TOKEN";


    @AllArgsConstructor
    @Getter
    public enum TokenType {
        ACCESS_TOKEN(ACCESS_TOKEN_EXPIRATION_TIME, ACCESS_TOKEN_COOKIE_NAME),
        REFRESH_TOKEN(REFRESH_TOKEN_EXPIRATION_TIME, REFRESH_TOKEN_COOKIE_NAME);

        private final int expirationTime;
        private final String cookieName;
    }

    /**
     * 사용자 id 번호, 이메일로 JWT 토큰 생성
     */
    public static String createToken(TokenType tokenType, Long id, String email) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenType.expirationTime))
                .withClaim("id", id)
                .withClaim("email", email)
                .sign(Algorithm.HMAC256(JwtProperties.SECRET));
    }

    public static Cookie createCookie(TokenType tokenType, String token, Boolean httpOnly) {
        Cookie cookie = new Cookie(tokenType.cookieName, token);
        cookie.setMaxAge(tokenType.expirationTime / 1000);
        cookie.setPath("/");
        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(true);

        return cookie;
    }

    /**
     * 요청 헤더에서 Access Token 추출
     */
    public static String extractAccessToken(String jwtHeader) {
        String accessToken = null;

        // Authorization 헤더 검증
        if (jwtHeader != null && jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {
            accessToken = jwtHeader.replace(JwtProperties.TOKEN_PREFIX, "");
        }

        return accessToken;
    }

    /**
     * Access Token 검증
     */
    public static Boolean validateAccessToken(String accessToken) {
        if (accessToken == null) return false;

        try {
            Long id = JWT.require(Algorithm.HMAC256(JwtProperties.SECRET))
                    .build()
                    .verify(accessToken)
                    .getClaim("id")
                    .asLong();
            return id != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    /**
     * 쿠키에서 Refresh Token 추출
     */
    public static String extractRefreshToken(Cookie[] cookies) {
        if (cookies == null || cookies.length == 0) return null;

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(JwtProperties.REFRESH_TOKEN_COOKIE_NAME))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    /**
     * Refresh Token 검증
     */
    public static Boolean validateRefreshToken(String refreshToken) {
        if (refreshToken == null) return false;

        try {
            Long id = JWT.require(Algorithm.HMAC256(JwtProperties.SECRET))
                    .build()
                    .verify(refreshToken)
                    .getClaim("id")
                    .asLong();
            return id != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
