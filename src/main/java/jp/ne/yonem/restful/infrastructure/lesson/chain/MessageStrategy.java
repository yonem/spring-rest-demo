package jp.ne.yonem.restful.infrastructure.lesson.chain;

import java.util.function.UnaryOperator;

/** メッセージ加工戦略の定義です。 */
public enum MessageStrategy {
  UPPER(s -> s.replace("[U]", "").toUpperCase()),
  TRIM(String::trim),
  DEFAULT(s -> s);

  private final UnaryOperator<String> operator;

  MessageStrategy(UnaryOperator<String> operator) {
    this.operator = operator;
  }

  public String apply(String text) {
    return operator.apply(text);
  }
}
