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

  public double modern(List<User> users) {
    var totalAge = users.stream().reduce(0.0, (age, user) -> age + user.age(), Double::sum);
    var adminCount = users.stream().filter(user -> "ADMIN".equals(user.role())).count();

    if (adminCount == 0) {
      return 0.0;
    }
    return totalAge / adminCount;
  }

  public double modern2(List<User> users) {

    // null安全の考慮（実務では Optional.ofNullable(users).stream()... とすることも多い）
    return users.stream()
        .filter(user -> "ADMIN".equals(user.role())) // ADMINだけに絞り込む
        .mapToInt(User::age) // age（int）のストリームに変換
        .average() // 平均を計算（結果は OptionalDouble）
        .orElse(0.0); // ADMINがいない場合は 0.0 を返す
  }
}
