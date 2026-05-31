package jp.ne.yonem.restful.infrastructure.lesson.state;

/** 受付中の状態です。 */
class OrderedState implements OrderState {

  @Override
  public String next(OrderContext context) {
    context.setState(new ProcessingState());
    return "注文を処理中に進めました。";
  }

  @Override
  public String cancel(OrderContext context) {
    return "注文をキャンセルしました。";
  }

  @Override
  public String getStatusName() {
    return "受付中";
  }
}
