package jp.ne.yonem.restful.infrastructure.validation;

import org.springframework.validation.FieldError;

public interface MessageResolverStrategy {
  boolean supports(String messageId);

  String resolveMessage(String messageId, Object target, FieldError fieldError);
}
