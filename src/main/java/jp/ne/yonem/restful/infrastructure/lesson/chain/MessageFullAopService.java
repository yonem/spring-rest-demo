package jp.ne.yonem.restful.infrastructure.lesson.chain;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/** AOPによる検閲とログ出力を組み合わせたサービスです。 */
@Service
@RequiredArgsConstructor
public class MessageFullAopService {

  private final ApplicationEventPublisher publisher;

  /**
   * メッセージを処理します。検閲とログ出力はAOPが担当します。
   *
   * @param input 入力メッセージ
   * @return 加工結果
   */
  @Censorship
  @ExecutionLog
  public String execute(String input) {
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
