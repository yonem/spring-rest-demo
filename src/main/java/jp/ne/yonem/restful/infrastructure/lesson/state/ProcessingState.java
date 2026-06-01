package jp.ne.yonem.restful.infrastructure.lesson.state;

/** 処理中の状態です。 */
class ProcessingState implements OrderState {

  @Override
  public String next(OrderContext context) {
    context.setState(new ShippedState());
    return "商品を発送しました。";
  }

  @Override
  public String cancel(OrderContext context) {
    return "処理中のため、キャンセルには管理者承認が必要です。";
  }

  @Override
  public String getStatusName() {
    return "処理中";
  }
}
