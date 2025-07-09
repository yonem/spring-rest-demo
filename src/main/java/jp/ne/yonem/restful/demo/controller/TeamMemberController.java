package jp.ne.yonem.restful.demo.controller;

import java.util.List;
import jp.ne.yonem.restful.demo.entity.Team;
import jp.ne.yonem.restful.demo.service.GetTeamMemberService;
import jp.ne.yonem.restful.demo.service.GetTeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TeamMemberController {

  private final GetTeamMemberService teamMemberService;
  private final GetTeamService teamService;

  @GetMapping("/member")
  public List<Team> getTeamMember() {
    return teamMemberService.execute();
  }

  @PostMapping("/team")
  public Team getTeam(@RequestParam(value = "id") Integer id) {
    return teamService.execute(id);
  }
}
