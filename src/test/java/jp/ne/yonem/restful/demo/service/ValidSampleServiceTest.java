package jp.ne.yonem.restful.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.HashSet;
import jp.ne.yonem.restful.demo.form.ValidSampleForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ValidSampleServiceTest {
  @Mock private Validator validator;

  @InjectMocks private ValidSampleService sut;

  @Test
  @DisplayName("violationsのmock")
  void test01() {
    var violations = getViolations();
    when(validator.validate(any())).thenReturn(violations);
    assertThrows(IllegalArgumentException.class, () -> sut.execute(new ValidSampleForm()));
  }

  private HashSet<ConstraintViolation<Object>> getViolations() {
    var violations = new HashSet<ConstraintViolation<Object>>();

    for (int i = 0; i < 50; i++) {
      var mock = mock(ConstraintViolation.class);
      when(mock.getMessage()).thenReturn("エラーメッセージ%02dです".formatted(i));
      violations.add(mock);
    }
    return violations;
  }
}
