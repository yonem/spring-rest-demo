package jp.ne.yonem.restful.demo.service;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;
import jp.ne.yonem.restful.demo.dto.DownloadFileResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class UserCsvService {
  private final List<User> users =
      List.of(new User("Alice", 25), new User("Bob", 30), new User("Charlie", 35));

  public DownloadFileResponse execute() throws IOException {

    try (var writer = new StringWriter()) {
      writer.write("name,age");
      writer.write(System.lineSeparator());
      writer.write(
          users.stream()
              .map(user -> "%s,%d".formatted(user.name(), user.age()))
              .collect(Collectors.joining(System.lineSeparator())));
      var header = new HttpHeaders();
      header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"users.csv\"");
      header.setContentType(MediaType.parseMediaType("text/csv;charset=UTF-8"));
      return new DownloadFileResponse(header, writer.toString().getBytes(UTF_8));
    }
  }

  public record User(String name, int age) {}
}
