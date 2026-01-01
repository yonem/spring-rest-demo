package jp.ne.yonem.restful.infrastructure.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Optional;
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
    return mapper
        .findById(form.getPolicyId())

        // 1. policyが存在しない場合の処理
        .map(Optional::of)
        .orElseGet(
            () -> {
              context.disableDefaultConstraintViolation();
              context.buildConstraintViolationWithTemplate("E999").addConstraintViolation();
              return Optional.empty();
            })

        // 2. policyは存在するが、バリデーションに失敗した場合の処理
        .filter(
            policy -> {
              if (policy.validate(form.getPassword())) {
                return true;
              }
              context.disableDefaultConstraintViolation();
              context
                  .buildConstraintViolationWithTemplate(this.messageId)
                  .addPropertyNode("password")
                  .addConstraintViolation();
              return false;
            })

        // 3. 全てのチェックを通過すればisPresentがtrueになる
        .isPresent();
  }
}
