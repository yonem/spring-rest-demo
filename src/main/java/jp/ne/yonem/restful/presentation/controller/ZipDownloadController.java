package jp.ne.yonem.restful.presentation.controller;

import java.io.IOException;
import java.util.Arrays;
import jp.ne.yonem.restful.infrastructure.ZipCreationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
@Slf4j
public class ZipDownloadController {
  private final ZipCreationService service;

  /**
   * ZIPファイルを生成し、クライアントにダウンロードさせるAPIエンドポイント。 Serviceから返されたDTOを利用。 * @param password ZIPに設定するパスワード
   *
   * @return ZIPファイルを含むResponseEntity
   */
  @GetMapping("/download/zip")
  public ResponseEntity<byte[]> downloadZip(@RequestParam String password) {
    var filePathsToZip = Arrays.asList("/tmp/user1.csv", "/tmp/user2.csv", "/tmp/user3.csv");

    try {
      var responseDto = service.execute(filePathsToZip, password);
      return ResponseEntity.ok()
          .headers(responseDto.getHeader())
          .contentType(MediaType.parseMediaType("application/zip"))
          .body(responseDto.getFile());

    } catch (IOException e) {
      log.error(e.getMessage(), e);
      return ResponseEntity.internalServerError().build();
    }
  }
}
