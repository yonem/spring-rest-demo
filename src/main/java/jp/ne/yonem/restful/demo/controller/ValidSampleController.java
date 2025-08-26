package jp.ne.yonem.restful.demo.controller;

import jakarta.validation.Valid;
import java.util.List;
import jp.ne.yonem.restful.demo.dto.CsvResponse;
import jp.ne.yonem.restful.demo.form.ValidSampleForm;
import jp.ne.yonem.restful.demo.service.ValidSampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/free")
@RequiredArgsConstructor
public class ValidSampleController {
  private final ValidSampleService service;

  @PostMapping("/valid")
  public String valid(@Valid @RequestBody ValidSampleForm form) {
    var res = new CsvResponse("", List.of());
    return "Validation successful.";
  }

  @PostMapping("/service-valid")
  public String validOnService(@RequestBody ValidSampleForm form) {
    service.execute(form);
    return "Validation successful.";
  }
}
