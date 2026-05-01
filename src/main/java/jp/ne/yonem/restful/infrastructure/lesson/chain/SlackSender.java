package jp.ne.yonem.restful.infrastructure.lesson.chain;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** 標準の送信先（Slack）です。 */
@Component
@Primary // 候補が複数ある場合、デフォルトでこちらが選ばれる
class SlackSender implements MessageSender {

  @Override
  public void send(String message) {
    System.out.println("Slack送信: " + message);
  }
}
