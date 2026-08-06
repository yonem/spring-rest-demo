package jp.ne.yonem.restful.infrastructure.lesson2.abstractfactory;

// --- Mobile用製品群 ---
final class MobileNotificationCard implements NotificationCard {
  @Override
  public String render() {
    return "{ \"type\": \"mobile_card\", \"title\": \"Mobile通知\" }";
  }
}
