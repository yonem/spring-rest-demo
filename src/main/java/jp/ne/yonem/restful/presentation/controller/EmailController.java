package jp.ne.yonem.restful.presentation.controller;

import jp.ne.yonem.restful.application.EmailService;
import jp.ne.yonem.restful.form.EmailForm;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmailController {

  private final EmailService service;

  @PostMapping("/email")
  public void sendEmail(@RequestBody EmailForm form) {
    service.execute(form, "test_mail", new Object[] {form.getTo()});
  }
}
