package jp.ne.yonem.restful.infrastructure.lesson2.adapter;

/** 新システムが使用する標準決済処理インターフェースです。 */
public interface PaymentProcessor {
  String processPayment(String accountId, int amountYen);
}
