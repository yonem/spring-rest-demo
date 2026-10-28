package jp.ne.yonem.restful.infrastructure.lesson2.chainofresponsibility;

import java.util.Objects;
import org.springframework.stereotype.Service;

/** Chain of Responsibilityパターンを利用して注文検証チェーンを実行するサービスです。 */
@Service
public class OrderValidationWorkflowService {

  /**
   * 指定されたハンドラーチェーンを用いて注文の検証を実行します。
   *
   * @param initialHandler チェーンの先頭ハンドラー
   * @param context 注文文脈
   * @return 検証結果メッセージ
   */
  public String execute(OrderHandler initialHandler, OrderContext context) {
    var safeHandler = Objects.requireNonNull(initialHandler, "initialHandler must not be null");
    var safeContext = Objects.requireNonNull(context, "context must not be null");

    return safeHandler.handle(safeContext);
  }
}
