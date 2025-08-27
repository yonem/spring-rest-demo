package jp.ne.yonem.restful.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import jp.ne.yonem.restful.demo.form.PasswordForm;
import jp.ne.yonem.restful.demo.mapper.PasswordPolicyMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PasswordPolicyValidator
    implements ConstraintValidator<PasswordPolicyCheck, PasswordForm> {
  private final PasswordPolicyMapper mapper;
  private String messageId;

  @Override
  public void initialize(PasswordPolicyCheck constraintAnnotation) {
    this.messageId = constraintAnnotation.message();
  }

  @Override
  public boolean isValid(PasswordForm form, ConstraintValidatorContext context) { // 引数名を変更
    var policy = mapper.findById(form.getPolicyId());

    if (Objects.isNull(policy)) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("E999").addConstraintViolation();
      return false;
    }
    var password = form.getPassword();
    context.disableDefaultConstraintViolation();

    if (password.length() < policy.getMin() || password.length() > policy.getMax()) {
      context
          .buildConstraintViolationWithTemplate(this.messageId)
          .addPropertyNode("password")
          .addConstraintViolation();
      return false;
    }
    var kinds = policy.getKinds();
    var combinationCount = 0;
    if (kinds.contains("l") && password.matches(".*[a-z].*")) combinationCount++;
    if (kinds.contains("u") && password.matches(".*[A-Z].*")) combinationCount++;
    if (kinds.contains("s") && password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*"))
      combinationCount++;
    if (kinds.contains("d") && password.matches(".*[0-9].*")) combinationCount++;

    var allowedChars = "";
    if (kinds.contains("l")) allowedChars += "a-z";
    if (kinds.contains("u")) allowedChars += "A-Z";
    if (kinds.contains("s")) allowedChars += "!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?";
    if (kinds.contains("d")) allowedChars += "0-9";

    var disallowedCharPattern = "[^" + allowedChars + "]+";
    if (password.matches(".*%s.*".formatted(disallowedCharPattern))) {
      context
          .buildConstraintViolationWithTemplate(this.messageId)
          .addPropertyNode("password")
          .addConstraintViolation();
      return false;
    }

    if (combinationCount < policy.getComb()) {
      context
          .buildConstraintViolationWithTemplate(this.messageId)
          .addPropertyNode("password")
          .addConstraintViolation();
      return false;
    }
    return true;
  }
}
