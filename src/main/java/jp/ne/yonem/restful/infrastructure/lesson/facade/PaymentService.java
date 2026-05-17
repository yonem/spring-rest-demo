package jp.ne.yonem.restful.infrastructure.lesson.facade;

import org.springframework.stereotype.Component;

/** 決済サブシステムです。 */
@Component
class PaymentService {
  public boolean process(int amount) {
    return true;
  }
}
