package jp.ne.yonem.restful.infrastructure.lesson2.abstractfactory;

/** 製品A: UI通知カードのインターフェースです。 */
public sealed interface NotificationCard permits WebNotificationCard, MobileNotificationCard {
  String render();
}
