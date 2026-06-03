package jp.ne.yonem.restful.infrastructure.lesson.state;

/** 完了（発送済）の状態です。 */
class ShippedState implements OrderState {

  @Override
  public String next(OrderContext context) {
    return "既に発送済です。";
  }

  @Override
  public String cancel(OrderContext context) {
    return "発送後のため、キャンセルはできません。";
  }

  @Override
  public String getStatusName() {
    return "発送済";
  }
}
