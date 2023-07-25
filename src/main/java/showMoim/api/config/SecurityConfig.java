package showMoim.api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.CorsFilter;
import showMoim.api.member.repository.MemberRepository;
import showMoim.api.security.filter.JwtAuthenticationFilter;
import showMoim.api.security.filter.JwtAuthorizationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;
    private final MemberRepository memberRepository;

    private static final String[] PUBLIC_API_PATTERN = {
        "/api/member/join/**",
        "/api/member/login"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilter(corsFilter)
            .formLogin().disable()
            .httpBasic().disable()
            .addFilter(getJWTAuthenticationFilter())
            .addFilter(new JwtAuthorizationFilter(authenticationManager(), memberRepository))
            .authorizeRequests()
            .antMatchers(PUBLIC_API_PATTERN).permitAll()
            .antMatchers("/api/member/**")
            .access("hasRole('ROLE_MEMBER') or hasRole('ROLE_ADMIN')")
            .antMatchers("/api/admin/**")
            .access("hasRole('ROLE_ADMIN')")
            .anyRequest().authenticated();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public JwtAuthenticationFilter getJWTAuthenticationFilter() throws Exception {
        final JwtAuthenticationFilter filter = new JwtAuthenticationFilter(authenticationManager());
        filter.setFilterProcessesUrl("/api/member/login");
        return filter;
    }
}
