package jp.ne.yonem.restful.infrastructure.persistence.mapper;

import java.util.Optional;
import jp.ne.yonem.restful.infrastructure.persistence.record.PasswordPolicy;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PasswordPolicyMapper {
  Optional<PasswordPolicy> findById(Integer id);
}
