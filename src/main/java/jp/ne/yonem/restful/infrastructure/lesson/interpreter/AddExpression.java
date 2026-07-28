package jp.ne.yonem.restful.infrastructure.lesson.interpreter;

import java.util.Objects;

/** 非終端表現: 加算演算を表す表現クラスです。 */
public record AddExpression(Expression left, Expression right) implements Expression {

  public AddExpression(Expression left, Expression right) {
    this.left = Objects.requireNonNull(left, "left must not be null");
    this.right = Objects.requireNonNull(right, "right must not be null");
  }

  @Override
  public int interpret(ExpressionContext context) {
    Objects.requireNonNull(context, "context must not be null");
    return this.left.interpret(context) + this.right.interpret(context);
  }
}
