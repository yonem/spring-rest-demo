package jp.ne.yonem.restful.service;

import java.util.List;
import jp.ne.yonem.restful.entity.Team;
import jp.ne.yonem.restful.mapper.TeamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetTeamMemberService {
  private final TeamMapper mapper;

  public List<Team> execute() {
    return mapper.findAllTeamsWithMembers();
  }
}
