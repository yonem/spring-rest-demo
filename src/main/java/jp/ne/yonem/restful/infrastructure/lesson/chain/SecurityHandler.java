package jp.ne.yonem.restful.infrastructure.lesson.chain;

import java.util.Objects;

/** セキュリティチェック（禁止ワード）担当。 */
class SecurityHandler extends MessageHandler {
  @Override
  public String handle(String message) {
    var safeMessage = Objects.requireNonNullElse(message, "");
    if (safeMessage.contains("NG_WORD")) {
      return "[REJECTED]";
    }
    return invokeNext(safeMessage);
  }
}
