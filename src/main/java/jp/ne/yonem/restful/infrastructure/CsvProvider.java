package jp.ne.yonem.restful.infrastructure;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import jp.ne.yonem.restful.presentation.dto.CsvResponse;
import jp.ne.yonem.restful.presentation.dto.DownloadFileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
@Slf4j
public class CsvProvider {

  public CsvResponse receive(Integer userId, MultipartFile csv) {

    try (var in = new BufferedReader(new InputStreamReader(csv.getInputStream(), "Shift-JIS"))) {
      var rows = new ArrayList<String>();
      var res = new CsvResponse();
      var line = in.readLine();
      res.setHeader(line);
      while (Objects.nonNull(line = in.readLine())) rows.add(line);
      res.setBody(rows);
      return res;

    } catch (Exception e) {
      log.error("CSV read failed.", e);
      throw new RuntimeException("CSV read failed.", e);
    }
  }

  private final List<User> users =
      List.of(new User("Alice", 25), new User("Bob", 30), new User("Charlie", 35));

  public DownloadFileResponse download() throws IOException {

    try (var writer = new StringWriter()) {
      writer.write("name,age");
      writer.write(System.lineSeparator());
      writer.write(
          users.stream()
              .map(user -> "%s,%d".formatted(user.name(), user.age()))
              .collect(Collectors.joining(System.lineSeparator())));
      var header = new HttpHeaders();
      header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"users.csv\"");
      header.setContentType(MediaType.parseMediaType("text/csv;charset=Shift-JIS"));
      return new DownloadFileResponse(header, writer.toString().getBytes(UTF_8));
    }
  }

  public record User(String name, int age) {}
}
