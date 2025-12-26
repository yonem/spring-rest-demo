package jp.ne.yonem.restful.infrastructure.validation;

import org.springframework.validation.FieldError;

public sealed interface MessageResolverStrategy
    permits StandardMessageResolver, PasswordPolicyMessageResolver {
  boolean supports(String messageId);

  String resolveMessage(String messageId, Object target, FieldError fieldError);
}
