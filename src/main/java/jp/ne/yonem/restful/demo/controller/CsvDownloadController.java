package jp.ne.yonem.restful.demo.controller;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import jp.ne.yonem.restful.demo.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class CsvDownloadController {
  private final UserService userService;

  public CsvDownloadController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/download/users.csv")
  public void downloadCsv(HttpServletResponse response) throws IOException {
    response.setContentType("text/csv; charset=UTF-8");
    response.setHeader("Content-Disposition", "attachment; filename=\"users.csv\"");

    try (var writer = response.getWriter()) {
      userService.execute(writer);
    }
  }
}
