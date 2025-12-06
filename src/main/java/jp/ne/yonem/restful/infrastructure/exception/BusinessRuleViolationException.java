package jp.ne.yonem.restful.infrastructure.exception;

import lombok.Getter;

@Getter
public class BusinessRuleViolationException extends RuntimeException {
  private final String messageKey; // 画面メッセージのキー
  private final Object[] messageArgs; // プレースホルダに埋める引数

  public BusinessRuleViolationException(String messageKey, Object... messageArgs) {
    super(messageKey); // ログのメッセージにもキーを残す
    this.messageKey = messageKey;
    this.messageArgs = messageArgs;
  }
}
