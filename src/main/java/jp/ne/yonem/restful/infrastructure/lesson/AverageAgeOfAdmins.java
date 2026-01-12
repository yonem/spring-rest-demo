package jp.ne.yonem.restful.infrastructure.lesson;

import java.util.List;

public class AverageAgeOfAdmins {
  public record User(long id, String name, int age, String role) {}

  public double legacy(List<User> users) {
    var totalAge = 0;
    var adminCount = 0;

    if (users != null) {
      for (var user : users) {
        if (user != null && "ADMIN".equals(user.role())) {
          totalAge += user.age();
          adminCount++;
        }
      }
    }

    if (adminCount == 0) {
      return 0.0;
    }
    return (double) totalAge / adminCount;
  }
}
