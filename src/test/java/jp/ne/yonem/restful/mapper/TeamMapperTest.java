package jp.ne.yonem.restful.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import jp.ne.yonem.restful.entity.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@MybatisTest
@Sql("classpath:sql/TeamMapperTest.sql")
@Transactional
class TeamMapperTest {
  @Autowired private TeamMapper sut; // MyBatis Mapperインターフェース

  @Test
  @DisplayName("Collection ResultMapの取得")
  void test1() {
    var result = sut.findAllTeamsWithMembers();
    assertNotNull(result);
    assertEquals("Hoge", result.getFirst().getMembers().getFirst().getName());
  }

  @Test
  @DisplayName("Teamの取得")
  void test2() {
    var result = sut.findById(1);
    assertThat(result).isNotNull();
    assertEquals("Team A", result.getName());
  }

  @Test
  @DisplayName("Teamの追加")
  void test3() {
    var insertTeam = new Team(null, "チーム!", null);
    assertEquals(1, sut.insert(insertTeam));
    assertEquals(10, insertTeam.getId());
  }
}
