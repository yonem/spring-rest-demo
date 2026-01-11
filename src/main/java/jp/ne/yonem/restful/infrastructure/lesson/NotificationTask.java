package jp.ne.yonem.restful.infrastructure.lesson;

public class NotificationTask {
  private String type; // "EMAIL", "SMS", "PUSH"
  private String address;
  private String phoneNumber;
  private String deviceToken;

  // Getter, Setter, Constructor が大量にあると想定してください

  public String getDestination() {
    if ("EMAIL".equals(type)) {
      return address;
    } else if ("SMS".equals(type)) {
      return phoneNumber;
    } else if ("PUSH".equals(type)) {
      return deviceToken;
    }
    return "UNKNOWN";
  }
}
