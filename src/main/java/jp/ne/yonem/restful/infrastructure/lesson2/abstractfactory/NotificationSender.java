package jp.ne.yonem.restful.infrastructure.lesson2.abstractfactory;

/** 製品B: 配信エンジンのインターフェースです。 */
public sealed interface NotificationSender permits WebNotificationSender, MobileNotificationSender {
  String send(String message);
}
