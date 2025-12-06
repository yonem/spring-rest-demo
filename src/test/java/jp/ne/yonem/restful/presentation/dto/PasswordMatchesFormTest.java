package jp.ne.yonem.restful.presentation.dto;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PasswordMatchesFormTest {
  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  @DisplayName("正常系")
  void test01() {
    var act = validator.validate(new PasswordMatchesForm("password", "password"));
    assertTrue(act.isEmpty());
  }

  @Test
  @DisplayName("異常系")
  void test02() {
    var act = validator.validate(new PasswordMatchesForm("password", "wrong password"));
    assertEquals(1, act.size());
    assertEquals("E004", act.stream().map(ConstraintViolation::getMessage).toList().getFirst());

    act = validator.validate(new PasswordMatchesForm(null, "wrong password"));
    assertEquals(1, act.size());

    act = validator.validate(new PasswordMatchesForm("password", null));
    assertEquals(1, act.size());

    act = validator.validate(new PasswordMatchesForm(null, null));
    assertEquals(1, act.size());
  }
}
