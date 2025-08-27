package jp.ne.yonem.restful.demo.mapper;

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
    assertEquals(1, act.getId());
    assertEquals(2, act.getComb());
    assertEquals(4, act.getMin());
    assertEquals(8, act.getMax());
    assertEquals("lusd", act.getKinds());
  }
}
