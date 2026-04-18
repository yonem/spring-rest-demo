package jp.ne.yonem.restful.infrastructure.lesson.chain;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/** 責任の連鎖を用いてメッセージを検閲・加工し、イベントを発行するサービスです。 */
@Service
@RequiredArgsConstructor
public class MessageChainService {

  private final ApplicationEventPublisher publisher;

  /**
   * メッセージをパイプラインで処理し、完了後にイベントを通知します。
   *
   * @param input 入力メッセージ
   */
  public void execute(String input) {
    var security = new SecurityHandler();
    var formatting = new FormattingHandler();

    security.setNext(formatting);

    var result = security.handle(input);
    publisher.publishEvent(new MessageProcessedEvent(result));
  }
}
