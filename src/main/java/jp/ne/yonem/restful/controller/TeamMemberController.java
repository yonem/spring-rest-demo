package jp.ne.yonem.restful.controller;

import java.util.List;
import jp.ne.yonem.restful.entity.Team;
import jp.ne.yonem.restful.service.GetTeamMemberService;
import jp.ne.yonem.restful.service.GetTeamService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

  @GetMapping("/team")
  public ResponseEntity<Team> getTeam(@RequestParam(value = "id") Integer id) {

    try {
      return new ResponseEntity<>(teamService.execute(id), HttpStatus.OK);
    } catch (NotFoundException e) {
      return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
  }
}
