package jp.ne.yonem.restful.demo.mapper;

import jp.ne.yonem.restful.demo.entity.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
  int insert(Member member);
}
