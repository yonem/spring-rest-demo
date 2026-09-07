package jp.ne.yonem.restful.infrastructure.lesson2.bridge;

/** Push通知送信用具象クラス */
public class PushNotificationSenderImplementor implements MessageSenderImplementor {

  @Override
  public String sendRawMessage(String title, String body) {
    return "[Push] %s: %s".formatted(title, body);
  }
}
