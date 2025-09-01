package jp.ne.yonem.restful.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jp.ne.yonem.restful.demo.form.PasswordMatchesForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class PasswordMatchesValidator
    implements ConstraintValidator<PasswordMatchesCheck, PasswordMatchesForm> {

  @Override
  public boolean isValid(PasswordMatchesForm form, ConstraintValidatorContext context) {

    try {
      return form.getPassword().equals(form.getRePassword());

    } catch (Exception e) {
      log.error("Passwords don't match.", e);
      return false;
    }
  }
}
