package jp.ne.yonem.restful.infrastructure.lesson.interpreter;

import java.util.Objects;

/** 終端表現: 具体的数値（定数）を表す表現クラスです。 */
public record NumberExpression(int number) implements Expression {

  @Override
  public int interpret(ExpressionContext context) {
    Objects.requireNonNull(context, "context must not be null");
    return this.number;
  }
}
