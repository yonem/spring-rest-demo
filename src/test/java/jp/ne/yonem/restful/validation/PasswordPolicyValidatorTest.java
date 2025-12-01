package jp.ne.yonem.restful.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;
import jp.ne.yonem.restful.entity.PasswordPolicy;
import jp.ne.yonem.restful.form.PasswordForm;
import jp.ne.yonem.restful.mapper.PasswordPolicyMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PasswordPolicyValidatorTest {
  @Mock private PasswordPolicyMapper passwordPolicyMapper;
  @Mock private ConstraintValidatorContext context;
  @Mock private ConstraintValidatorContext.ConstraintViolationBuilder builder;
  @Mock private NodeBuilderCustomizableContext propertyNodeBuilder;
  @InjectMocks private PasswordPolicyValidator validator;

  private PasswordPolicy validPolicy;

  @BeforeEach
  void setUp() {
    validPolicy = new PasswordPolicy(1, 8, 16, "lusd", 3);
    var mockAnnotation = Mockito.mock(PasswordPolicyCheck.class);
    when(mockAnnotation.message()).thenReturn("E001");
    validator.initialize(mockAnnotation);
    lenient().when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
    lenient().when(builder.addPropertyNode(anyString())).thenReturn(propertyNodeBuilder);
    lenient().when(propertyNodeBuilder.addConstraintViolation()).thenReturn(null);
    lenient().when(passwordPolicyMapper.findById(1)).thenReturn(validPolicy);
  }

  @Test
  @DisplayName("正常系")
  void test01() {
    when(passwordPolicyMapper.findById(1)).thenReturn(validPolicy);

    var form = new PasswordForm(1, "Password123!");
    assertTrue(validator.isValid(form, context));
  }

  @Test
  @DisplayName("ポリシーIDが存在しない")
  void test02() {
    when(passwordPolicyMapper.findById(99)).thenReturn(null);

    var form = new PasswordForm(99, "AnyPassword123!");
    assertFalse(validator.isValid(form, context));
  }

  @Test
  @DisplayName("パスワードが短すぎる")
  void test03() {
    when(passwordPolicyMapper.findById(1)).thenReturn(validPolicy);

    var form = new PasswordForm(1, "Short1!");
    assertFalse(validator.isValid(form, context));
  }

  @Test
  @DisplayName("パスワードが長すぎる")
  void test04() {
    when(passwordPolicyMapper.findById(1)).thenReturn(validPolicy);

    var form = new PasswordForm(1, "Password12345678!");
    assertFalse(validator.isValid(form, context));
  }

  @Test
  @DisplayName("文字種が足りない")
  void test05() {
    when(passwordPolicyMapper.findById(1)).thenReturn(validPolicy);

    var form = new PasswordForm(1, "password123");
    assertFalse(validator.isValid(form, context));
  }

  @Test
  @DisplayName("指定された文字種以外を使用している")
  void test06() {
    when(passwordPolicyMapper.findById(1)).thenReturn(validPolicy);

    var form = new PasswordForm(1, "Password123!日本語");
    assertFalse(validator.isValid(form, context));
  }
}
