package jp.ne.yonem.restful.validation;

import java.util.List;
import java.util.Objects;
import jp.ne.yonem.restful.controller.MessageUtil;
import jp.ne.yonem.restful.form.PasswordForm;
import jp.ne.yonem.restful.mapper.PasswordPolicyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

@Component
@RequiredArgsConstructor
public class PasswordPolicyMessageResolver implements MessageResolverStrategy {
  private final MessageUtil messageUtil;
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
      return messageUtil.getMessage(messageId, args);
    }

    return messageUtil.getMessage(messageId);
  }

  private Integer getPolicyIdFromTarget(Object target) {
    if (target instanceof PasswordForm) {
      return ((PasswordForm) target).getPolicyId();
    }
    return null;
  }
}
