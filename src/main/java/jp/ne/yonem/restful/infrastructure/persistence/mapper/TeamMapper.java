package jp.ne.yonem.restful.infrastructure.persistence.mapper;

import java.util.List;
import java.util.Optional;
import jp.ne.yonem.restful.infrastructure.persistence.record.Team;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TeamMapper {
  List<Team> findAllTeamsWithMembers();

  Optional<Team> findById(@Param("id") Integer id);

  int insert(Team team);
}
