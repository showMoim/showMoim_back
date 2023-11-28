package showMoim.api.security;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import showMoim.api.domain.member.dto.MemberJoinDto.RegisterForm;
import showMoim.api.domain.member.dto.MemberLoginDto;
import showMoim.api.domain.member.service.MemberService;
import showMoim.api.global.security.JwtProperties;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * JWT 관련 인증 테스트
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class JwtAuthTest {
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MemberService memberService;

    private MockMvc mockMvc;

    private final String API_URL = "http://localhost:8080";
    private final String TEST_EMAIL_1 = "auth_test_hello1@asd.com";
    private final String TEST_PW_1 = "123456";

    // 각 테스트 전에 실행
    @BeforeEach
    public void before() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();


        RegisterForm joinRegisterForm = RegisterForm.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PW_1)
                .passwordConfirm(TEST_PW_1)
                .build();

        memberService.join(joinRegisterForm);
    }


    @Test
    @WithMockUser
    void 회원_로그인_테스트() throws Exception {
        Gson gson = new Gson();

        // given
        MemberLoginDto.Form loginForm = MemberLoginDto.Form.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PW_1)
                .build();

        // when
        mockMvc.perform(post("/api/member/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(loginForm)))
                // then
                .andExpect(MockMvcResultMatchers.status().isOk())
                // .andExpect(MockMvcResultMatchers.header().exists(JwtProperties.HEADER_STRING))
                .andExpect(MockMvcResultMatchers.cookie().exists(JwtProperties.ACCESS_TOKEN_COOKIE_NAME))
                .andExpect(MockMvcResultMatchers.cookie().exists(JwtProperties.REFRESH_TOKEN_COOKIE_NAME))
                .andDo(MockMvcResultHandlers.print());
    }
}
