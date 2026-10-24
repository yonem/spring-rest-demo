package jp.ne.yonem.restful.infrastructure.lesson2.chainofresponsibility;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/** 処理対象となる注文リクエスト情報です。 */
@Getter
public class OrderContext {

  private final String orderId;
  private final int amount;
  @Setter private boolean stockAvailable = true;
  @Setter private boolean creditApproved = true;

  public OrderContext(String orderId, int amount) {
    this.orderId = Objects.requireNonNull(orderId, "orderId must not be null");
    if (amount <= 0) {
      throw new IllegalArgumentException("amount must be greater than 0");
    }
    this.amount = amount;
  }
}
