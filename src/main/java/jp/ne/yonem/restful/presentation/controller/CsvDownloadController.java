package jp.ne.yonem.restful.presentation.controller;

import java.io.IOException;
import jp.ne.yonem.restful.application.UserCsvDownloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class CsvDownloadController {
  private final UserCsvDownloadService userService;

  @GetMapping("/download/users.csv")
  public ResponseEntity<?> downloadCsv() throws IOException {
    var csv = userService.execute();
    return ResponseEntity.ok().headers(csv.getHeader()).body(csv.getFile());
  }
}
