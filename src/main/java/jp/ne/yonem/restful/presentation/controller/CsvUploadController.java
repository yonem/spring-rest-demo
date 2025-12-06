package jp.ne.yonem.restful.presentation.controller;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import jakarta.validation.Validator;
import java.io.IOException;
import jp.ne.yonem.restful.application.UserCsvUploadService;
import jp.ne.yonem.restful.presentation.dto.CsvUploadForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class CsvUploadController {
  private final UserCsvUploadService userCsvUploadService;
  private final Validator validator;
  private final MessageUtil messageUtil;

  @PostMapping(value = "/free/upload/users.csv", consumes = MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> uploadUsersCsv(@RequestPart String name, @RequestPart MultipartFile file)
      throws IOException {
    var violations = validator.validate(new CsvUploadForm(name, file));

    if (!violations.isEmpty()) {
      var messages =
          violations.stream().map(e -> messageUtil.getResponse(e.getMessage(), 3, 10)).toList();
      return ResponseEntity.badRequest().body(messages);
    }
    var users = userCsvUploadService.execute(file);
    return ResponseEntity.ok(users);
  }
}
