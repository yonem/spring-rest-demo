package jp.ne.yonem.restful.infrastructure.lesson2.chainofresponsibility;

/** 在庫チェックを行うハンドラーです。 */
public class StockCheckHandler extends OrderHandler {

  @Override
  protected String process(OrderContext context) {
    if (!context.isStockAvailable()) {
      return "FAILED: Out of stock";
    }
    return null; // 通過
  }
}
