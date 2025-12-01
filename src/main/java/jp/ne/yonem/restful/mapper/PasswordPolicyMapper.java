package jp.ne.yonem.restful.mapper;

import jp.ne.yonem.restful.entity.PasswordPolicy;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PasswordPolicyMapper {
  PasswordPolicy findById(Integer id);
}
