package jp.ne.yonem.restful.infrastructure.lesson2.abstractfactory;

/** Mobile用のコンポーネント群を生成する具体的な工場です。 */
public class MobileComponentFactory implements NotificationComponentFactory {

  @Override
  public NotificationCard createCard() {
    return new MobileNotificationCard();
  }

  @Override
  public NotificationSender createSender() {
    return new MobileNotificationSender();
  }
}
