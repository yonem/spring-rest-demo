package jp.ne.yonem.restful.infrastructure.lesson.chain;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/** 複数の送信先を使い分けるメッセージ配信サービスです。 */
@Service
@RequiredArgsConstructor
public class MessageDispatchService {

  @Qualifier("slackSender")
  private final MessageSender primarySender;

  @Qualifier("emailSender")
  private final MessageSender specificSender;

  /**
   * メッセージを適切な送信先に振り分けます。
   *
   * @param message 対象メッセージ
   */
  public void execute(String message) {
    primarySender.send(message);

    if (Objects.nonNull(message) && message.contains("URGENT")) {
      specificSender.send(message);
    }
  }
}
