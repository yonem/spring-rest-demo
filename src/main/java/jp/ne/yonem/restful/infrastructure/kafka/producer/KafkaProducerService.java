package jp.ne.yonem.restful.infrastructure.kafka.producer;

import jp.ne.yonem.restful.infrastructure.kafka.config.KafkaConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
  private final KafkaTemplate<String, String> kafkaTemplate;

  public void sendMessage(String message) {
    kafkaTemplate.send(KafkaConfig.TOPIC_NAME_1, message);
  }
}
