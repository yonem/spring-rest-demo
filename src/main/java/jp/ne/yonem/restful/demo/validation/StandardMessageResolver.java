package jp.ne.yonem.restful.demo.validation;

import java.util.List;
import java.util.Objects;
import jp.ne.yonem.restful.demo.controller.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

@Component
@RequiredArgsConstructor
public class StandardMessageResolver implements MessageResolverStrategy {
  private final MessageUtil messageUtil;
  private final List<String> LENGTH_CHECK_IDS = List.of("E002");

  @Override
  public boolean supports(String messageId) {
    return true;
  }

  @Override
  public String resolveMessage(String messageId, Object target, FieldError fieldError) {
    var args = new Object[0];

    // FieldErrorが存在する場合
    if (Objects.nonNull(fieldError)) {

      // FieldErrorの引数リストを元にargsを構築
      var originalArgs = Objects.requireNonNull(fieldError.getArguments());
      var field = originalArgs[0];
      args = new Object[] {field};

      // 例: @Lengthや@Sizeアノテーションのメッセージを解決
      if (3 <= originalArgs.length) {
        args = new Object[] {originalArgs[2], originalArgs[3], field};

        if (LENGTH_CHECK_IDS.contains(messageId)) {
          // @Lengthのアノテーション属性は、通常、インデックス2と3に格納される
          var min = originalArgs[3];
          var max = originalArgs[2];
          args = new Object[] {min, max, field};
        }

      } else if (2 == originalArgs.length) {
        // E002以外の、標準的なプレースホルダーを持つメッセージに対応
        args = new Object[] {originalArgs[1], field};
      }
    }
    return messageUtil.getMessage(messageId, args);
  }
}
