package jp.ne.yonem.restful.infrastructure.lesson2.abstractfactory;

/** 関連する通知関連オブジェクト群を生成する抽象工場インターフェースです。 */
public interface NotificationComponentFactory {

  NotificationCard createCard();

  NotificationSender createSender();
}
