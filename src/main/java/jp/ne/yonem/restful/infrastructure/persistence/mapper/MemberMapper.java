package jp.ne.yonem.restful.infrastructure.persistence.mapper;

import jp.ne.yonem.restful.infrastructure.persistence.record.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
  int insert(Member member);
}
