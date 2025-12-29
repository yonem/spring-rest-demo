package jp.ne.yonem.restful.infrastructure.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import jp.ne.yonem.restful.infrastructure.persistence.mapper.PasswordPolicyMapper;
import jp.ne.yonem.restful.presentation.dto.PasswordForm;
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
  public boolean isValid(PasswordForm form, ConstraintValidatorContext context) {
    var policy = mapper.findById(form.getPolicyId());

    if (Objects.isNull(policy)) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("E999").addConstraintViolation();
      return false;
    }

    // 判定ロジックをモデルに委譲
    if (!policy.validate(form.getPassword())) {
      context.disableDefaultConstraintViolation();
      context
          .buildConstraintViolationWithTemplate(this.messageId)
          .addPropertyNode("password")
          .addConstraintViolation();
      return false;
    }
    return true;
  }
}
