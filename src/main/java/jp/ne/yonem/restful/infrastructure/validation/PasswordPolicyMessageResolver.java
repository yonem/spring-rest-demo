package jp.ne.yonem.restful.infrastructure.validation;

import java.util.List;
import java.util.Optional;
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
    return Optional.ofNullable(target)

        // 1. targetがPasswordFormであれば、policyIdを取得する
        .filter(PasswordForm.class::isInstance)
        .map(PasswordForm.class::cast)
        .map(PasswordForm::getPolicyId)

        // 2. policyIdが存在すれば、mapperで検索する
        .flatMap(mapper::findById)

        // 3. policyが存在すれば、引数付きでメッセージを解決する
        .map(
            policy -> {
              var args = new Object[] {policy.min(), policy.max(), policy.kinds(), policy.comb()};
              return messageUtil.getMessage(messageId, args);
            })

        // 4. 上記のいずれかが空（null）なら、引数なしのデフォルトメッセージを返す
        .orElseGet(() -> messageUtil.getMessage(messageId));
  }
}
