package jp.ne.yonem.restful.demo.service;

import java.util.Objects;
import jp.ne.yonem.restful.demo.entity.Team;
import jp.ne.yonem.restful.demo.mapper.TeamMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetTeamService {
  private final TeamMapper mapper;

  public Team execute(Integer id) throws NotFoundException {
    var team = mapper.findById(id);

    if (Objects.isNull(team)) {
      log.error("Team is not found.");
      throw new NotFoundException("Team is not found.");
    }
    return team;
  }
}
