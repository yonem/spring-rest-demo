package jp.ne.yonem.restful.mapper;

import jp.ne.yonem.restful.entity.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
  int insert(Member member);
}
