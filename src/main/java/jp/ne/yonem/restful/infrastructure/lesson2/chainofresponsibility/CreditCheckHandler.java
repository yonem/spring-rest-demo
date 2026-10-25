package jp.ne.yonem.restful.infrastructure.lesson2.chainofresponsibility;

/** 与信（決済枠）チェックを行うハンドラーです。 */
public class CreditCheckHandler extends OrderHandler {

  @Override
  protected String process(OrderContext context) {
    if (!context.isCreditApproved()) {
      return "FAILED: Credit limit exceeded";
    }
    return null; // 通過
  }
}
