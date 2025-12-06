package jp.ne.yonem.restful.infrastructure.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
  public static final String TOPIC_NAME_1 = "my-topic-1";
  public static final String TOPIC_NAME_2 = "my-topic-2";

  //  @Bean
  public NewTopic topic1() {
    return TopicBuilder.name(TOPIC_NAME_1).partitions(1).replicas(1).build();
  }

  //  @Bean
  public NewTopic topic2() {
    return TopicBuilder.name(TOPIC_NAME_2).partitions(1).replicas(1).build();
  }
}
