package jp.ne.yonem.restful.infrastructure.persistence.mapper;

import static org.junit.jupiter.api.Assertions.*;

import jp.ne.yonem.restful.infrastructure.persistence.record.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

@MybatisTest
@Sql("classpath:sql/MemberMapperTest.sql")
class MemberMapperTest {
  @Autowired private MemberMapper sut;

  @Test
  @DisplayName("Member insert")
  void test1() {
    assertEquals(1, sut.insert(new Member(null, 1, "member1")));
  }
}
