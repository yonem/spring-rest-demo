package jp.ne.yonem.restful.infrastructure.lesson.state;

/** 注文の状態に応じた振る舞いを定義するインターフェースです。 */
public interface OrderState {
  String next(OrderContext context);

  String cancel(OrderContext context);

  String getStatusName();
}
