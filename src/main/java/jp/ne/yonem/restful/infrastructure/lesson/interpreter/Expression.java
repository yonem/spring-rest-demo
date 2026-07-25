package jp.ne.yonem.restful.infrastructure.lesson.interpreter;

/** 構文木のすべてのノードが実装する抽象表現インターフェースです。 */
public interface Expression {
  int interpret(ExpressionContext context);
}
