package jp.ne.yonem.restful.infrastructure.lesson;

import java.util.List;
import java.util.Optional;

/** コレクションの検索と「存在しない場合」のハンドリング */
public class GetUserEmail {

  public record User(Integer id, String email) {}

  public String legacy(List<User> users, long targetId) {
    String email = null;

    if (users != null) {
      for (User user : users) {
        if (user.id() == targetId) {
          if (user.email() != null) {
            email = user.email();
          }
          break;
        }
      }
    }

    if (email == null) {
      return "No Email";
    } else {
      return email;
    }
  }

  public String modern(List<User> users, long targetId) {
    return Optional.ofNullable(users).stream()
        .flatMap(List::stream)
        .filter(user -> user.id() == targetId)
        .findFirst()
        .map(User::email)
        .filter(email -> !email.isBlank())
        .orElse("No Email");
  }
}
