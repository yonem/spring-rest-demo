package jp.ne.yonem.restful.infrastructure.validation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PasswordPolicyValidator.class)
@Target({TYPE})
@Retention(RUNTIME)
public @interface PasswordPolicyCheck {
  String message() default "E001";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
