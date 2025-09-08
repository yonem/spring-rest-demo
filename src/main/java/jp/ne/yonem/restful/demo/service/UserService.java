package jp.ne.yonem.restful.demo.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final List<User> users =
      List.of(new User("Alice", 25), new User("Bob", 30), new User("Charlie", 35));

  public void execute(PrintWriter writer) throws IOException {
    writer.println("name,age");
    users.forEach(user -> writer.println("%s,%d".formatted(user.name(), user.age())));
  }

  public record User(String name, int age) {}
}
