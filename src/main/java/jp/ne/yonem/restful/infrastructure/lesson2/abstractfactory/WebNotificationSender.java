package jp.ne.yonem.restful.infrastructure.lesson2.abstractfactory;

final class WebNotificationSender implements NotificationSender {
  @Override
  public String send(String message) {
    return "[WebPush配信] " + message;
  }
}
