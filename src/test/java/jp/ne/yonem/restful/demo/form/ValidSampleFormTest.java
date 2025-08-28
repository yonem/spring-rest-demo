package jp.ne.yonem.restful.demo.form;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ValidSampleFormTest {
  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  @DisplayName("正常パターン")
  public void test01() {
    var form = new ValidSampleForm("I001", "message");
    var act = validator.validate(form);
    assertTrue(act.isEmpty());
  }

  @Test
  @DisplayName("異常パターン")
  public void test02() {
    var form = new ValidSampleForm();
    var act = validator.validate(form);
    assertEquals(2, act.size());
  }
}
