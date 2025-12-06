package jp.ne.yonem.restful.infrastructure.kafka.controller;

import jp.ne.yonem.restful.infrastructure.kafka.producer.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class KafkaController {
  private final KafkaProducerService kafkaProducerService;

  @PostMapping("/kafka")
  public String sendMessage(@RequestParam("message") String message) {
    kafkaProducerService.sendMessage(message);
    return "Message sent: " + message;
  }
}
