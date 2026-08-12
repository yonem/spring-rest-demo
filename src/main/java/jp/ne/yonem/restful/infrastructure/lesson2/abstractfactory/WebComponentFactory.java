package jp.ne.yonem.restful.infrastructure.lesson2.abstractfactory;

/** Web用のコンポーネント群を生成する具体的な工場です。 */
public class WebComponentFactory implements NotificationComponentFactory {

  @Override
  public NotificationCard createCard() {
    return new WebNotificationCard();
  }

  @Override
  public NotificationSender createSender() {
    return new WebNotificationSender();
  }
}
