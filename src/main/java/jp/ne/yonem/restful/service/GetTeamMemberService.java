package jp.ne.yonem.restful.service;

import java.util.List;
import jp.ne.yonem.restful.infrastructure.persistence.mapper.TeamMapper;
import jp.ne.yonem.restful.infrastructure.persistence.record.Team;
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
