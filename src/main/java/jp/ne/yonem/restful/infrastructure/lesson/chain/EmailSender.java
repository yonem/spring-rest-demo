package jp.ne.yonem.restful.infrastructure.lesson.chain;

import org.springframework.stereotype.Component;

/** 特定のケースで使用する送信先（Email）です。 */
@Component("emailSender") // Bean名を明示
class EmailSender implements MessageSender {

  @Override
  public void send(String message) {
    System.out.println("Email送信: " + message);
  }
}
