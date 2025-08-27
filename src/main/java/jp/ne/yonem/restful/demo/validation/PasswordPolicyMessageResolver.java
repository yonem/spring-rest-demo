package jp.ne.yonem.restful.demo.validation;

import java.util.List;
import java.util.Objects;
import jp.ne.yonem.restful.demo.form.PasswordForm;
import jp.ne.yonem.restful.demo.mapper.PasswordPolicyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

@Component
@RequiredArgsConstructor
public class PasswordPolicyMessageResolver implements MessageResolverStrategy {
  private final MessageSource messageSource;
  private final PasswordPolicyMapper mapper;
  private final List<String> PWD_POLICY_IDS = List.of("E001");

  @Override
  public boolean supports(String messageId) {
    return PWD_POLICY_IDS.contains(messageId);
  }

  @Override
  public String resolveMessage(String messageId, Object target, FieldError fieldError) {
    var policyId = getPolicyIdFromTarget(target);
    var policy = Objects.nonNull(policyId) ? mapper.findById(policyId) : null;

    if (Objects.nonNull(policy)) {
      var args =
          new Object[] {policy.getMin(), policy.getMax(), policy.getKinds(), policy.getComb()};
      return messageSource.getMessage(messageId, args, LocaleContextHolder.getLocale());
    }

    return messageSource.getMessage(messageId, null, LocaleContextHolder.getLocale());
  }

  private Integer getPolicyIdFromTarget(Object target) {
    if (target instanceof PasswordForm) {
      return ((PasswordForm) target).getPolicyId();
    }
    return null;
  }
}
