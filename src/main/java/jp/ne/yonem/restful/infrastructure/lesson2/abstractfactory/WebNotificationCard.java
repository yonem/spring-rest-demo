package jp.ne.yonem.restful.infrastructure.lesson2.abstractfactory;

// --- Web用製品群 ---
final class WebNotificationCard implements NotificationCard {

  @Override
  public String render() {
    return "<div class='web-card'>Web通知</div>";
  }
}
