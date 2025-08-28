package jp.ne.yonem.restful.demo.form;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class AccountFormTest {
  @Autowired private Validator validator;
  @Autowired private JdbcTemplate jdbcTemplate;

  @BeforeEach
  public void setUp() {
    jdbcTemplate.execute(
        "INSERT INTO password_policy (id, min, max, kinds, comb) VALUES (1, 8, 16, 'lusd', 3)");
  }

  @Test
  @DisplayName("正常系")
  public void test01() {
    var form = new AccountForm(1, "Password123!", "123");
    var act = validator.validate(form);
    assertTrue(act.isEmpty());
  }

  @Test
  @DisplayName("異常系")
  public void test02() {

    // 文字数不足
    var form = new AccountForm(1, "Pass11!", "12");
    var act = validator.validate(form);
    assertEquals(2, act.size());

    // 文字数超過
    form = new AccountForm(1, "Password12345678!", "1234567890!");
    act = validator.validate(form);
    assertEquals(2, act.size());

    // 空文字
    form = new AccountForm(1, "", "");
    act = validator.validate(form);
    assertEquals(4, act.size());

    // ポリシーが存在しない
    form = new AccountForm(99, "Password123!", "123");
    act = validator.validate(form);
    assertEquals(1, act.size());

    // 文字種不足
    form = new AccountForm(1, "password!", "123");
    act = validator.validate(form);
    assertEquals(1, act.size());

    // 指定文字種以外
    form = new AccountForm(1, "Pass123日本語!", "123");
    act = validator.validate(form);
    assertEquals(1, act.size());
  }
}
