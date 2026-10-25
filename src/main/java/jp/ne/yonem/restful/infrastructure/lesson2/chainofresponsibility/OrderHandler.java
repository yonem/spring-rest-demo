package jp.ne.yonem.restful.infrastructure.lesson2.chainofresponsibility;

import java.util.Objects;

/** 連鎖するハンドラーの抽象基底クラスです。 */
public abstract class OrderHandler {

  private OrderHandler next;

  public OrderHandler setNext(OrderHandler next) {
    this.next = next;
    return next; // チェーン構築を流れるように書けるよう next を返却
  }

  public String handle(OrderContext context) {
    var safeContext = Objects.requireNonNull(context, "context must not be null");

    // 自身の処理を実行
    var result = process(safeContext);

    // 処理がNG（または処理中断）の場合は終了し、OKで次のハンドラーがあれば連鎖
    if (result != null) {
      return result;
    }
    if (next != null) {
      return next.handle(safeContext);
    }
    return "SUCCESS: Order processed";
  }

  /**
   * 各具象ハンドラーが実装する個別検証・処理ロジックです。
   *
   * @param context 注文文脈
   * @return 処理失敗時はエラーメッセージ、通過時は null
   */
  protected abstract String process(OrderContext context);
}
