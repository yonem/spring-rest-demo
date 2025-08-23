package jp.ne.yonem.restful.demo.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;
import jp.ne.yonem.restful.demo.dto.CsvResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReceiveCsvService {

  public CsvResponse execute(Integer userId, MultipartFile csv) {

    try (var in = new BufferedReader(new InputStreamReader(csv.getInputStream(), "Shift-JIS"))) {
      var rows = new ArrayList<String>();
      var res = new CsvResponse();
      var line = in.readLine();
      res.setHeader(line);

      while (Objects.nonNull(line = in.readLine())) {
        rows.add(line);
      }
      res.setBody(rows);
      return res;

    } catch (Exception e) {
      log.error("CSV read failed.", e);
      throw new RuntimeException("CSV read failed.", e);
    }
  }
}
