package jp.ne.yonem.restful.application;

import static com.google.zxing.common.StringUtils.SHIFT_JIS;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserCsvUploadService {

  public List<User> execute(MultipartFile file) throws IOException {
    var format =
        CSVFormat.DEFAULT
            .builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .setTrim(true) // ヘッダーとデータの両端の空白を除去
            .setIgnoreHeaderCase(true) // ヘッダー名の大文字小文字を無視
            .build();

    try (var reader = new InputStreamReader(file.getInputStream(), SHIFT_JIS);
        var parser = new CSVParser(reader, format)) {
      var users = new ArrayList<User>();

      for (var record : parser) {
        var name = record.get("氏名");
        var age = Integer.parseInt(record.get("年齢"));
        users.add(new User(name, age));
      }
      return users;
    }
  }

  public record User(String name, int age) {}
}
