package jp.ne.yonem.restful.infrastructure.lesson.interpreter;

import java.util.Objects;
import org.springframework.stereotype.Service;

/** インタープリタパターンによる構文木評価を統括するサービスです。 */
@Service
public class ExpressionEvaluationService {

  /**
   * 渡された構文木（Expression）を指定のコンテキストで評価し、計算結果を取得します。
   *
   * @param expression 評価対象の構文木
   * @param context 評価文脈
   * @return 計算結果の数値
   */
  public int execute(Expression expression, ExpressionContext context) {
    var safeExpression = Objects.requireNonNull(expression, "expression must not be null");
    var safeContext = Objects.requireNonNull(context, "context must not be null");

    return safeExpression.interpret(safeContext);
  }
}
