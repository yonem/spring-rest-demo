package jp.ne.yonem.restful.demo.controller;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class CsvDownloadController {

  private final List<User> users;

  // 通常はサービス層からデータを取得しますが、今回はシンプルにするためハードコード
  public CsvDownloadController() {
    this.users = List.of(new User("Alice", 25), new User("Bob", 30), new User("Charlie", 35));
  }

  @GetMapping("/download/users.csv")
  public void downloadCsv(HttpServletResponse response) throws IOException {
    response.setContentType("text/csv; charset=UTF-8");
    response.setHeader("Content-Disposition", "attachment; filename=\"users.csv\"");

    try (var writer = response.getWriter()) {
      writer.println("name,age");
      for (var user : users) {
        writer.printf("%s,%d\n", user.name, user.age);
      }
    }
  }

  public record User(String name, int age) {}
}
