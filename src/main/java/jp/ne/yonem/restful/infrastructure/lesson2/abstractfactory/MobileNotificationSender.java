package jp.ne.yonem.restful.infrastructure.lesson2.abstractfactory;

final class MobileNotificationSender implements NotificationSender {

  @Override
  public String send(String message) {
    return "[APNs/FCM配信] " + message;
  }
}
