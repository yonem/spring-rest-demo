package jp.ne.yonem.restful.infrastructure.lesson.state;

import java.util.Objects;
import org.springframework.stereotype.Service;

/** 注文状態を操作するサービスです。 */
@Service
public class OrderStateService {

  /**
   * 注文のフェーズを次に進めます。
   *
   * @param context 注文コンテキスト
   * @return 実行結果メッセージ
   */
  public String execute(OrderContext context) {
    var safeContext = Objects.requireNonNull(context, "context must not be null");
    return safeContext.getState().next(safeContext);
  }
}
