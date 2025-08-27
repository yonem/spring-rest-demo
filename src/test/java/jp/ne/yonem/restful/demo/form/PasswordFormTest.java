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
class PasswordFormTest {
  @Autowired private Validator validator;
  @Autowired private JdbcTemplate jdbcTemplate;

  @BeforeEach
  public void setUp() {
    jdbcTemplate.execute(
        "INSERT INTO password_policy (id, min, max, kinds, comb) VALUES (1, 8, 16, 'lusd', 3)");
  }

  @Test
  @DisplayName("パスワードポリシーを満たす場合")
  public void testValidPassword() {
    var form = new PasswordForm(1, "Password123!", "name");
    var violations = validator.validate(form);
    assertTrue(violations.isEmpty());
  }

  @Test
  @DisplayName("文字数が少なすぎる場合")
  public void testPasswordTooShort() {
    var form = new PasswordForm(1, "Pass11!", "name");
    var violations = validator.validate(form);
    assertFalse(violations.isEmpty());

    var violation = violations.iterator().next();
    assertEquals("E001", violation.getMessageTemplate());
  }

  @Test
  @DisplayName("文字数が多すぎる場合")
  public void testPasswordTooLong() {
    var form = new PasswordForm(1, "Password12345678!", "name");
    var violations = validator.validate(form);
    assertFalse(violations.isEmpty());
    var violation = violations.iterator().next();
    assertEquals("E001", violation.getMessageTemplate());
  }

  @Test
  @DisplayName("文字種が足りない場合 (数字がない)")
  public void testPasswordMissingKinds() {
    var form = new PasswordForm(1, "password!", "name");
    var violations = validator.validate(form);
    assertFalse(violations.isEmpty());
    var violation = violations.iterator().next();
    assertEquals("E001", violation.getMessageTemplate());
  }

  @Test
  @DisplayName("指定された文字種以外を使用している場合")
  public void testPasswordContainsInvalidKinds() {
    var form = new PasswordForm(1, "password123日本語", "name");
    var violations = validator.validate(form);
    assertFalse(violations.isEmpty());
    var violation = violations.iterator().next();
    assertEquals("E001", violation.getMessageTemplate());
  }
}
