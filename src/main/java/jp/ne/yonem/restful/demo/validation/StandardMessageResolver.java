package jp.ne.yonem.restful.demo.validation;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

@Component
@RequiredArgsConstructor
public class StandardMessageResolver implements MessageResolverStrategy {
  private final MessageSource messageSource;

  @Override
  public boolean supports(String messageId) {
    return true;
  }

  @Override
  public String resolveMessage(String messageId, Object target, FieldError fieldError) {
    Object[] args = new Object[0];

    // FieldErrorが存在する場合
    if (Objects.nonNull(fieldError)) {
      // FieldErrorの引数リストを元にargsを構築
      Object[] originalArgs = fieldError.getArguments();

      // 例: @Lengthや@Sizeアノテーションのメッセージを解決
      if ("E002".equals(messageId) && originalArgs.length >= 4) {
        // @Lengthのアノテーション属性は、通常、インデックス2と3に格納される
        Object min = originalArgs[2];
        Object max = originalArgs[3];
        args = new Object[] {min, max};
      } else {
        // E002以外の、標準的なプレースホルダーを持つメッセージに対応
        args = originalArgs;
      }
    }
    return messageSource.getMessage(messageId, args, LocaleContextHolder.getLocale());
  }
}
