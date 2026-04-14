package jp.ne.yonem.restful.infrastructure.lesson.chain;

import java.util.Optional;
import lombok.Setter;

/** メッセージ検閲・加工の基底ハンドラーです。 */
public abstract class MessageHandler {

  @Setter protected MessageHandler next;

  public abstract String handle(String message);

  protected String invokeNext(String message) {
    return Optional.ofNullable(next).map(h -> h.handle(message)).orElse(message);
  }
}
