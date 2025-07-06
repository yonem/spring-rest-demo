package jp.ne.yonem.restful.demo.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HelloController.class)
class HelloControllerTest {

  @Autowired private MockMvc mockMvc;

  @Test
  @DisplayName("正常系: get:/api/hello?name=???")
  void test1() throws Exception {
    var res =
        mockMvc
            .perform(get("/api/hello?name=user").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();
    assertEquals("Hello, user!", res.getContentAsString());
  }
}
