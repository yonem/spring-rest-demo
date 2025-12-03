package jp.ne.yonem.restful.infrastructure.persistence.mapper;

import jp.ne.yonem.restful.infrastructure.persistence.record.PasswordPolicy;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PasswordPolicyMapper {
  PasswordPolicy findById(Integer id);
}
