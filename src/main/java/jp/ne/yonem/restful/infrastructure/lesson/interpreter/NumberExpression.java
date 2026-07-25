package jp.ne.yonem.restful.infrastructure.lesson.interpreter;

import java.util.Objects;

/** 終端表現: 具体的数値（定数）を表す表現クラスです。 */
public class NumberExpression implements Expression {

  private final int number;

  public NumberExpression(int number) {
    this.number = number;
  }

  @Override
  public int interpret(ExpressionContext context) {
    Objects.requireNonNull(context, "context must not be null");
    return this.number;
  }
}
