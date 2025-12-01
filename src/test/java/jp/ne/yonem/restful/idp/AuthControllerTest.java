package jp.ne.yonem.restful.idp;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.ne.yonem.restful.controller.MessageUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
class AuthControllerTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @MockitoBean private AuthenticationManager authenticationManager;
  @MockitoBean private JwtTokenProvider tokenProvider;
  @MockitoBean private MessageUtil messageUtil;

  @Test
  @DisplayName("正常系: /api/auth/signin")
  @WithMockUser
  void test01() throws Exception {
    var loginRequest = new LoginRequest();
    loginRequest.setUsername("user");
    loginRequest.setPassword("pass");
    var authentication = new UsernamePasswordAuthenticationToken("user", null);
    when(authenticationManager.authenticate(any())).thenReturn(authentication);
    when(tokenProvider.generateToken(authentication)).thenReturn("dummy-jwt");

    mockMvc
        .perform(
            post("/api/auth/signin")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").value("dummy-jwt"));
  }

  @Test
  @DisplayName("正常系: /api/auth/verify-sso-token")
  @WithMockUser
  void test02() throws Exception {
    LoginResponse ssoToken = new LoginResponse("dummy-jwt");
    when(tokenProvider.validateToken("dummy-jwt")).thenReturn(true);
    when(tokenProvider.getLoginIdFromJWT("dummy-jwt")).thenReturn("user");

    mockMvc
        .perform(
            post("/api/auth/verify-sso-token")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ssoToken)))
        .andExpect(status().isOk())
        .andExpect(
            content()
                .string(org.hamcrest.Matchers.containsString("SSO token verified for user: user")));
  }

  @Test
  @DisplayName("異常系: /api/auth/verify-sso-token ")
  @WithMockUser
  void test03() throws Exception {
    LoginResponse ssoToken = new LoginResponse("invalid-jwt");
    when(tokenProvider.validateToken("invalid-jwt")).thenReturn(false);

    mockMvc
        .perform(
            post("/api/auth/verify-sso-token")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ssoToken)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(org.hamcrest.Matchers.containsString("Invalid SSO token")));
  }
}
