package jp.ne.yonem.restful.presentation.controller;

import static org.springframework.http.MediaType.*;

import jp.ne.yonem.restful.application.ReceiveCsvService;
import jp.ne.yonem.restful.presentation.dto.CsvResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/free")
@RequiredArgsConstructor
public class ReceiveCsvController {
  private final ReceiveCsvService service;

  @PostMapping(value = "/csv", consumes = MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<CsvResponse> postMessage(
      @RequestParam("id") Integer id, @RequestPart("csv") MultipartFile csv) {
    return ResponseEntity.ok().body(service.execute(id, csv));
  }
}
