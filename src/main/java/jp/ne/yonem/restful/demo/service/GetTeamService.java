package jp.ne.yonem.restful.demo.service;

import jp.ne.yonem.restful.demo.entity.Team;
import jp.ne.yonem.restful.demo.mapper.TeamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetTeamService {
  private final TeamMapper mapper;

  public Team execute(Integer id) {
    return mapper.findById(id);
  }
}
