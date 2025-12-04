package jp.ne.yonem.restful.presentation.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import jp.ne.yonem.restful.application.GetTeamMemberService;
import jp.ne.yonem.restful.application.GetTeamService;
import jp.ne.yonem.restful.infrastructure.persistence.record.Member;
import jp.ne.yonem.restful.infrastructure.persistence.record.Team;
import org.apache.ibatis.javassist.NotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TeamMemberController.class)
class TeamMemberControllerTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @MockitoBean private GetTeamService getTeamService;
  @MockitoBean private GetTeamMemberService getTeamMemberService;
  @MockitoBean private MessageUtil messageUtil;

  private static Team team;

  @BeforeAll
  static void setup() {
    team = new Team(1, "team1", List.of(new Member(1, 1, "user1"), new Member(2, 1, "user2")));
  }

  @Test
  @DisplayName("正常系: チーム取得")
  @WithMockUser(roles = "USER")
  void test01() throws Exception {
    when(getTeamService.execute(team.getId())).thenReturn(team);

    mockMvc
        .perform(get("/api/team?id=1").contentType(MediaType.APPLICATION_JSON))
        .andExpectAll(status().isOk(), content().json(objectMapper.writeValueAsString(team)));
  }

  @Test
  @DisplayName("異常系: チーム取得: 存在しないチーム")
  @WithMockUser(roles = "USER")
  void test02() throws Exception {
    doThrow(NotFoundException.class).when(getTeamService).execute(any());

    mockMvc
        .perform(get("/api/team?id=1").contentType(MediaType.APPLICATION_JSON))
        .andExpectAll(status().isBadRequest());
  }
}
