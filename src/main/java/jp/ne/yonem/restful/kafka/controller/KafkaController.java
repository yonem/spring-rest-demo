package jp.ne.yonem.restful.kafka.controller;

import jp.ne.yonem.restful.kafka.producer.KafkaProducerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class KafkaController {
  private final KafkaProducerService kafkaProducerService;

  public KafkaController(KafkaProducerService kafkaProducerService) {
    this.kafkaProducerService = kafkaProducerService;
  }

  @PostMapping("/kafka")
  public String sendMessage(@RequestParam("message") String message) {
    kafkaProducerService.sendMessage(message);
    return "Message sent: " + message;
  }
}
