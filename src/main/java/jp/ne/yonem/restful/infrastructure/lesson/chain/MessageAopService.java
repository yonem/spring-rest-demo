package jp.ne.yonem.restful.infrastructure.lesson.chain;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/** AOPによる自動検閲を利用するメッセージサービスです。 */
@Service
@RequiredArgsConstructor
public class MessageAopService {

  private final ApplicationEventPublisher publisher;

  /**
   * メッセージを処理します。検閲はAOPによって自動的に行われます。
   *
   * @param input 入力メッセージ
   */
  @Censorship
  public String execute(String input) {

    // 既に検閲済みの状態でビジネスロジックに到達する
    var strategy =
        switch (input) {
          case String s when s.startsWith("[U]") -> MessageStrategy.UPPER;
          default -> MessageStrategy.DEFAULT;
        };

    var result = strategy.apply(input);
    publisher.publishEvent(new MessageProcessedEvent(result));
    return result;
  }
}
