package jp.ne.yonem.restful.presentation.dto;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserFormTest {

  private Validator validator;

  @BeforeEach
  void setUp() {
    try (var factory = Validation.buildDefaultValidatorFactory()) {
      validator = factory.getValidator();
    }
  }

  @Test
  @DisplayName("正常系")
  void test1() {
    var userRequest = new UserForm("valid_user", 20);
    var violations = validator.validate(userRequest);
    assertThat(violations).isEmpty();
  }

  @Test
  @DisplayName("ユーザ名が未入力")
  void test2() {
    var userRequest = new UserForm("", 25);
    var violations = validator.validate(userRequest);
    assertThat(violations).isNotEmpty();
    assertThat(violations).anyMatch(v -> v.getMessage().equals("ユーザー名は必須です"));
  }

  @Test
  @DisplayName("年齢の下限チェック")
  void test3() {
    var userRequest = new UserForm("test_user", 17);
    var violations = validator.validate(userRequest);
    assertThat(violations).isNotEmpty();
    assertThat(violations).anyMatch(v -> v.getMessage().equals("年齢は18歳以上である必要があります"));
  }
}
