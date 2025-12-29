package jp.ne.yonem.restful.infrastructure.persistence.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@MybatisTest
@Sql("classpath:sql/PasswordPolicyMapperTest.sql")
@Transactional
class PasswordPolicyMapperTest {
  @Autowired private PasswordPolicyMapper sut;

  @Test
  @DisplayName("Collection ResultMapの取得")
  void test1() {
    var act = sut.findById(1);
    assertNotNull(act);
    assertEquals(1, act.id());
    assertEquals(2, act.comb());
    assertEquals(4, act.min());
    assertEquals(8, act.max());
    assertEquals("lusd", act.kinds());
  }
}
