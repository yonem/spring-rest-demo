package jp.ne.yonem.restful.infrastructure.validation;

import java.util.List;
import java.util.Objects;
import jp.ne.yonem.restful.infrastructure.MessageUtil;
import jp.ne.yonem.restful.infrastructure.persistence.mapper.PasswordPolicyMapper;
import jp.ne.yonem.restful.presentation.dto.PasswordForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

@Component
@RequiredArgsConstructor
public final class PasswordPolicyMessageResolver implements MessageResolverStrategy {
  private final MessageUtil messageUtil;
  private final PasswordPolicyMapper mapper;
  private final List<String> PWD_POLICY_IDS = List.of("E001");

  @Override
  public boolean supports(String messageId) {
    return PWD_POLICY_IDS.contains(messageId);
  }

  @Override
  public String resolveMessage(String messageId, Object target, FieldError fieldError) {
    var policyId =
        switch (target) {
          case PasswordForm f -> ((PasswordForm) target).getPolicyId();
          default -> null;
        };
    var policy = Objects.nonNull(policyId) ? mapper.findById(policyId) : null;

    if (Objects.nonNull(policy)) {
      var args =
          new Object[] {policy.getMin(), policy.getMax(), policy.getKinds(), policy.getComb()};
      return messageUtil.getMessage(messageId, args);
    }

    return messageUtil.getMessage(messageId);
  }
}
