package jp.ne.yonem.restful.demo.controller;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import java.io.IOException;
import java.util.List;
import jp.ne.yonem.restful.demo.service.UserCsvUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class CsvUploadController {
  private final UserCsvUploadService userCsvUploadService;

  @PostMapping(value = "/free/upload/users.csv", consumes = MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<List<UserCsvUploadService.User>> uploadUsersCsv(
      @RequestPart("file") MultipartFile file) throws IOException {

    if (file.isEmpty()) {
      return ResponseEntity.badRequest().body(null);
    }
    var users = userCsvUploadService.execute(file);
    return ResponseEntity.ok(users);
  }
}
