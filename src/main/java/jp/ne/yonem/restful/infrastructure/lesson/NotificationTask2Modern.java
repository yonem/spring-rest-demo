package jp.ne.yonem.restful.infrastructure.lesson;

public class NotificationTask2Modern {
  public sealed interface Notification permits Email, Sms, Push {}

  public record Email(String address) implements Notification {}

  public record Sms(String phoneNumber) implements Notification {}

  public record Push(String deviceToken) implements Notification {}

  public String getDestination(Notification notification) {
    return switch (notification) {
      case Email e -> e.address();
      case Sms s -> s.phoneNumber();
      case Push p -> p.deviceToken();
    };
  }
}
