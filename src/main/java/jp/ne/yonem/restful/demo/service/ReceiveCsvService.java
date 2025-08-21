package jp.ne.yonem.restful.demo.service;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReceiveCsvService {

  public List<String> execute(Integer userId, MultipartFile csv) {

    try (var in = new BufferedReader(new InputStreamReader(csv.getInputStream(), UTF_8))) {
      var rows = new ArrayList<String>();
      var line = in.readLine();

      while (Objects.nonNull(line)) {
        rows.add(line);
        line = in.readLine();
      }
      return rows;

    } catch (Exception e) {
      log.error("CSV read failed.", e);
      throw new RuntimeException("CSV read failed.", e);
    }
  }
}
