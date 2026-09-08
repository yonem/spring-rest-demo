package jp.ne.yonem.restful.infrastructure.lesson2.bridge;

/** Email送信用具象クラス */
public class EmailSenderImplementor implements MessageSenderImplementor {

  @Override
  public String sendRawMessage(String title, String body) {
    return "[Email] %s: %s".formatted(title, body);
  }
}
