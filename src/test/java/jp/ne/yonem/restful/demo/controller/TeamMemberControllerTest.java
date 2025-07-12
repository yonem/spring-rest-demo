package jp.ne.yonem.restful.demo.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import jp.ne.yonem.restful.demo.entity.Member;
import jp.ne.yonem.restful.demo.entity.Team;
import jp.ne.yonem.restful.demo.service.GetTeamMemberService;
import jp.ne.yonem.restful.demo.service.GetTeamService;
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
  @MockitoBean private GetTeamService getTeamService;
  @MockitoBean private GetTeamMemberService getTeamMemberService;

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
        .andExpectAll(
            status().isOk(),
            jsonPath("$.id").value(team.getId()),
            jsonPath("$.name").value(team.getName()),
            jsonPath("$.members.length()", is(2)),
            jsonPath("$.members[0].id").value(team.getMembers().getFirst().getId()),
            jsonPath("$.members[1].id").value(team.getMembers().getLast().getId()));
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
