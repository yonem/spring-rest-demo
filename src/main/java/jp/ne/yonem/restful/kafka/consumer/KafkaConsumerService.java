package jp.ne.yonem.restful.kafka.consumer;

import jp.ne.yonem.restful.kafka.config.KafkaConfig;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

  @KafkaListener(topics = KafkaConfig.TOPIC_NAME, groupId = "my-group")
  public void consume(String message) {
    System.out.println("Received message: " + message);
  }
}
