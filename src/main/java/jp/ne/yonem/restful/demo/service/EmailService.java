package jp.ne.yonem.restful.demo.service;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.InputStreamReader;
import jp.ne.yonem.restful.demo.form.EmailForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
  private final JavaMailSender mailSender;
  private final ResourceLoader resourceLoader;

  @Value("${system.mail.from}")
  private String mailFrom;

  public void execute(EmailForm form, String content, Object[] placeHolders) {
    var message = new SimpleMailMessage();
    message.setFrom(mailFrom);
    message.setTo(form.getTo());
    message.setSubject(form.getSubject());
    var loader = resourceLoader.getResource("classpath:mail/%s.txt".formatted(content));

    try (var in = new InputStreamReader(loader.getInputStream(), UTF_8)) {
      var body = FileCopyUtils.copyToString(in);
      message.setText(body.formatted(placeHolders));
      mailSender.send(message);
      log.info("Email sent successfully to: {}", form.getTo());

    } catch (Exception e) {
      log.error("Failed to send email to: {}", form.getTo(), e);
      throw new RuntimeException("Email sending failed", e);
    }
  }
}
