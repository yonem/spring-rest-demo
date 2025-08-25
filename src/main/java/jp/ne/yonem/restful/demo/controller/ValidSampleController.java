package jp.ne.yonem.restful.demo.controller;

import jakarta.validation.Valid;
import jp.ne.yonem.restful.demo.form.ValidSampleForm;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/free")
@RequiredArgsConstructor
public class ValidSampleController {

  @GetMapping("/valid")
  public String getAllPosts(@Valid @RequestBody ValidSampleForm form) {
    return "Validation successful.";
  }
}
