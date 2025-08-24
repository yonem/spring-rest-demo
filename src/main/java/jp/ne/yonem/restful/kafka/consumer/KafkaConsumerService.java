package jp.ne.yonem.restful.kafka.consumer;

import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

  //  @KafkaListener(topics = KafkaConfig.TOPIC_NAME_1, groupId = "group-a")
  public void consumeGroupA(String message) {
    System.out.println("Received message by Group A: " + message);
  }

  //  @KafkaListener(topics = KafkaConfig.TOPIC_NAME_1, groupId = "group-b")
  public void consumeGroupB(String message) {
    System.out.println("Received message by Group B: " + message);
  }
}
