package jp.ne.yonem.restful.demo.mapper;

import java.util.List;
import jp.ne.yonem.restful.demo.entity.Team;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TeamMapper {
  List<Team> findAllTeamsWithMembers();

  Team findById(@Param("id") Integer id);

  int insert(Team team);
}
