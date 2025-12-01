package jp.ne.yonem.restful.validation;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public enum ErrorMessages {
  W100100("W100100", "バリデーションエラーが発生しました"),
  W100101("W100101", "必須項目 {0} が不足しています"),
  W100102("W100102", "入力項目 {0} の形式が不正です");

  private final String messageId;
  private final String message;

  private static final Map<String, ErrorMessages> ID_MAP =
      Arrays.stream(values())
          .collect(Collectors.toMap(ErrorMessages::getMessageId, Function.identity()));

  ErrorMessages(String messageId, String message) {
    this.messageId = messageId;
    this.message = message;
  }

  public static ErrorMessages of(String messageId) {
    return ID_MAP.get(messageId);
  }
}
