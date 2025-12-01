package jp.ne.yonem.restful.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HelloController.class)
class HelloControllerTest {
  @Autowired private MockMvc mockMvc;
  @MockitoBean private MessageUtil messageUtil;

  @Test
  @DisplayName("正常系: get:/api/hello?name=???")
  @WithMockUser(roles = "USER")
  void test1() throws Exception {
    var res =
        mockMvc
            .perform(get("/api/hello?name=user").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();
    assertEquals("Hello, user!", res.getContentAsString());
  }

  @Test
  @DisplayName("異常系: get:/api/hello?name=（パラメータなし）")
  @WithMockUser(roles = "USER")
  void test2() throws Exception {
    var res =
        mockMvc
            .perform(get("/api/hello").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();
    assertEquals("Hello, Guest!", res.getContentAsString());
  }

  @Test
  @DisplayName("正常系: post:/api/message（JSONボディ）")
  @WithMockUser(roles = "USER")
  void test3() throws Exception {
    var json = "{\"content\":\"テストメッセージ\"}";
    var res =
        mockMvc
            .perform(
                post("/api/message")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();
    assertEquals("Received your message: 'テストメッセージ'. Server says hi!", res.getContentAsString());
  }
}
