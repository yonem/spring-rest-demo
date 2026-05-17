package jp.ne.yonem.restful.infrastructure.lesson.facade;

import org.springframework.stereotype.Component;

/** 配送サブシステムです。 */
@Component
class ShippingService {
  public void reserve(String productId) {
    System.out.println("Shipping reserved.");
  }
}
