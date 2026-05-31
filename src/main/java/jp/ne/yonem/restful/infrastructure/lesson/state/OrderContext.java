package jp.ne.yonem.restful.infrastructure.lesson.state;

import lombok.Data;

/** 状態を管理するコンテキストクラスです。 */
@Data
public class OrderContext {
  private OrderState state = new OrderedState();
}
